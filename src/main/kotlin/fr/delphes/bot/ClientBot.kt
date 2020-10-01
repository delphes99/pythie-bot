package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import com.github.twitch4j.helix.webhooks.domain.WebhookRequest
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import fr.delphes.VIPParser
import fr.delphes.bot.webserver.webhook.TwitchWebhook
import fr.delphes.configuration.BotConfiguration
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.VIPListReceived
import mu.KotlinLogging
import java.time.Duration

class ClientBot(
    configuration: BotConfiguration,
    val publicUrl: String
) {
    val channels = mutableListOf<Channel>()
    private val botCredential = OAuth2Credential("twitch", "oauth:${configuration.botAccountOauth}")
    val secretKey = configuration.secretKey

    val client = TwitchClientBuilder.builder()
        .withClientId(configuration.clientId)
        .withClientSecret(secretKey)
        .withEnablePubSub(true)
        .withEnableHelix(true)
        .withEnableChat(true)
        .withChatAccount(botCredential)
        .build()!!

    val chat = client.chat

    init {
        chat.connect()
        client.pubSub.connect()

        val eventHandler = client.eventManager.getEventHandler(SimpleEventHandler::class.java)
        eventHandler.onEvent(ChannelMessageEvent::class.java, ::handleChannelMessageEvent)
        eventHandler.onEvent(RewardRedeemedEvent::class.java, ::handleRewardRedeemedEvent)
        eventHandler.onEvent(IRCMessageEvent::class.java, ::handleIRCMessage)
    }

    fun register(channel: Channel) {
        channels.add(channel)

        chat.joinChannel(channel.name);
        client.pubSub.listenForChannelPointsRedemptionEvents(botCredential, channel.userId)
    }

    fun subscribeWebhooks() {
        //TODO clean old subscription
        //TODO cron refresh sub
        channels.forEach { channel ->
            TwitchWebhook.forEach { twitchWebhook ->
                try {
                    client.helix.requestWebhookSubscription(
                        WebhookRequest(
                            "${publicUrl}/${channel.name}/${twitchWebhook.callSuffix}",
                            "subscribe",
                            twitchWebhook.topic(channel.userId),
                            WEBHOOK_DURATION,
                            "toto"
                        ),
                        channel.oAuth
                    ).execute()
                    LOGGER.info { "Subscription for twich webhook ${twitchWebhook.name} for ${channel.name} ok" }
                } catch (e: Exception) {
                    LOGGER.info(e) { "Subscription for twich webhook ${twitchWebhook.name} for ${channel.name} failed" }
                }
            }
        }
    }

    private fun handleRewardRedeemedEvent(event: RewardRedeemedEvent) {
        val channel = findChannelById(event.redemption.channelId)
        channel.handleRewardRedeemedEvent(RewardRedemption(event))
    }

    private fun handleChannelMessageEvent(event: ChannelMessageEvent) {
        val channel = findChannelByName(event.channel.name)
        channel.handleChannelMessage(MessageReceived(event))
    }

    private fun handleIRCMessage(event: IRCMessageEvent) {
        //TODO check
        val channel = findChannelByName(event.channelName.get())
        event.message?.also {
            if (it.isPresent) {
                val vipResult = VIPParser.extractVips(it.get())
                if (vipResult is VIPParser.VIPResult.VIPList) {
                    val vipListReceived = VIPListReceived(vipResult.users)

                    channel.handleVIPListReceived(vipListReceived)
                }
            }
        }
    }

    private fun findChannelByName(name: String) =
        channels.first { channel -> channel.name == name }

    private fun findChannelById(id: String) =
        channels.first { channel -> channel.userId == id }

    companion object {
        //private val WEBHOOK_DURATION = Duration.ofDays(1).toSeconds().toInt()
        private val WEBHOOK_DURATION = Duration.ofMinutes(5).toSeconds().toInt()
        private val LOGGER = KotlinLogging.logger {}
    }
}
