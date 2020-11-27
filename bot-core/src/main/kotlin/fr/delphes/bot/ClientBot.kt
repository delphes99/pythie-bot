package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.TwitchChat
import com.github.twitch4j.helix.webhooks.domain.WebhookRequest
import fr.delphes.bot.webserver.webhook.TwitchWebhook
import fr.delphes.configuration.BotConfiguration
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.TwitchAppCredential
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.time.Duration

class ClientBot(
    configuration: BotConfiguration,
    private val publicUrl: String,
    val configFilepath: String
) {
    val channels = mutableListOf<Channel>()
    private val botCredential = OAuth2Credential("twitch", "oauth:${configuration.botAccountOauth}")

    val appCredential = TwitchAppCredential.of(
        configuration.clientId,
        configuration.secretKey,
        tokenRepository = { getToken -> AuthTokenRepository("${configFilepath}\\auth\\bot.json", getToken) }
    )

    //TODO manage secret sign
    private val twitchApi = AppTwitchClient.build(appCredential, "secret")

    val client = TwitchClientBuilder.builder()
        .withClientId(appCredential.clientId)
        .withClientSecret(appCredential.clientSecret)
        .withEnableHelix(true)
        .withEnableChat(true)
        .withChatAccount(botCredential)
        .build()!!

    val chat: TwitchChat = client.chat

    init {
        chat.connect()
        runBlocking {
            twitchApi.removeAllWebhooks()
        }
    }

    fun findChannelBy(name: String): Channel? {
        return channels.find { channel -> channel.name == name }
    }

    fun register(channel: Channel) {
        channels.add(channel)

        chat.joinChannel(channel.name)
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
                        channel.channelCredential.authToken!!.access_token
                    ).execute()
                    //TODO catch failed subscription
                    LOGGER.debug { "Twich webhook ${twitchWebhook.name} for ${channel.name} : Subscription request sent" }
                } catch (e: Exception) {
                    LOGGER.error (e) { "Twich webhook ${twitchWebhook.name} for ${channel.name} : Subscription request failed" }
                }
            }
        }
    }

    companion object {
        //private val WEBHOOK_DURATION = Duration.ofDays(1).toSeconds().toInt()
        private val WEBHOOK_DURATION = Duration.ofMinutes(2).toSeconds().toInt()
        private val LOGGER = KotlinLogging.logger {}
    }
}
