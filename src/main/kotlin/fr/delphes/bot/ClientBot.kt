package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.helix.webhooks.domain.WebhookRequest
import fr.delphes.bot.webserver.webhook.TwitchWebhook
import fr.delphes.configuration.BotConfiguration
import mu.KotlinLogging
import java.time.Duration

class ClientBot(
    configuration: BotConfiguration,
    val publicUrl: String
) {
    val channels = mutableListOf<Channel>()
    val botCredential = OAuth2Credential("twitch", "oauth:${configuration.botAccountOauth}")

    val clientId = configuration.clientId
    val secretKey = configuration.secretKey

    val client = TwitchClientBuilder.builder()
        .withClientId(clientId)
        .withClientSecret(secretKey)
        .withEnableHelix(true)
        .withEnableChat(true)
        .withChatAccount(botCredential)
        .build()!!

    val chat = client.chat

    init {
        chat.connect()
    }

    fun register(channel: Channel) {
        channels.add(channel)

        chat.joinChannel(channel.name);
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
                    //TODO catch failed subscription
                    LOGGER.info { "Subscription for twich webhook ${twitchWebhook.name} for ${channel.name} ok" }
                } catch (e: Exception) {
                    LOGGER.info(e) { "Subscription for twich webhook ${twitchWebhook.name} for ${channel.name} failed" }
                }
            }
        }
    }

    companion object {
        //private val WEBHOOK_DURATION = Duration.ofDays(1).toSeconds().toInt()
        private val WEBHOOK_DURATION = Duration.ofMinutes(5).toSeconds().toInt()
        private val LOGGER = KotlinLogging.logger {}
    }
}
