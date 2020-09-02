package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.TwitchChat
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import fr.delphes.VIPParser
import fr.delphes.configuration.Configuration
import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.IncomingEvent
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.incoming.RewardRedemption
import fr.delphes.event.incoming.VIPListReceived
import fr.delphes.feature.Feature

data class Bot(
    val channel: String,
    val features: List<Feature>,
    val twitchClient: TwitchClient,
    val ownerTwitchClient: TwitchClient
) {
    init {
        val eventHandler = twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java)
        eventHandler.onEvent(ChannelMessageEvent::class.java, ::handleChannelMessage)

        eventHandler.onEvent(RewardRedeemedEvent::class.java) {
            handleRewardRedeemedEvent(it)
        }
        eventHandler.onEvent(IRCMessageEvent::class.java) {
            handleIRCMessage(it)
        }
    }

    companion object {
        fun build(
            channel: String,
            channelId: String,
            configuration: Configuration,
            features: List<Feature>
        ): Bot {
            val botCredential = OAuth2Credential("twitch", configuration.botAccountOauth)
            val ownerCredential = OAuth2Credential("twitch", configuration.ownerAccountOauth)

            val baseClientBuilder = TwitchClientBuilder.builder()
                .withClientId(configuration.clientId)
                .withClientSecret(configuration.secretKey)

            val twitchClient = baseClientBuilder
                .withEnablePubSub(true)
                .withEnableHelix(true)
                .withEnableChat(true)
                .withChatAccount(botCredential)
                .build()

            val ownerTwitchClient = baseClientBuilder
                .withEnableChat(true)
                .withChatAccount(ownerCredential)
                .build()

            val chat = twitchClient.chat

            chat.connect()
            chat.joinChannel(channel);

            twitchClient.pubSub.eventManager
            twitchClient.pubSub.connect()
            twitchClient.pubSub.listenForChannelPointsRedemptionEvents(botCredential, channelId)

            return Bot(
                channel,
                features,
                twitchClient,
                ownerTwitchClient
            )
        }
    }

    private val chat: TwitchChat get() = twitchClient.chat
    private val ownerChat: TwitchChat get() = ownerTwitchClient.chat

    private val rewardRedeptionHandlers = features.flatMap(Feature::rewardHandlers)
    private val vipListReceivedHandlers = features.flatMap(Feature::vipListReceivedHandlers)
    private val messageReceivedHandlers = features.flatMap(Feature::messageReceivedHandlers)

    private fun handleRewardRedeemedEvent(event: RewardRedeemedEvent) {
        rewardRedeptionHandlers.handleEvent(RewardRedemption(event))
    }

    private fun handleIRCMessage(event: IRCMessageEvent) {
        event.message?.also {
            if (it.isPresent) {
                val vipResult = VIPParser.extractVips(it.get())
                if(vipResult is VIPParser.VIPResult.VIPList) {
                    val vipListReceived = VIPListReceived(vipResult.users)

                    vipListReceivedHandlers.handleEvent(vipListReceived)
                }
            }
        }
    }

    private fun handleChannelMessage(event: ChannelMessageEvent) {
        val incomingEvent = MessageReceived(event.user.name, event.message)

        messageReceivedHandlers.handleEvent(incomingEvent)
    }

    private fun <T: IncomingEvent> List<EventHandler<T>>.handleEvent(rewardRedemption: T) {
        val outgoingEvents = flatMap { handler -> handler.handle(rewardRedemption) }

        outgoingEvents.forEach { e ->
            e.applyOnTwitch(chat, ownerChat, channel)
        }
    }
}