package fr.delphes.bot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.TwitchChat
import fr.delphes.configuration.BotConfiguration
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.TwitchAppCredential
import kotlinx.coroutines.runBlocking

class ClientBot(
    configuration: BotConfiguration,
    val publicUrl: String,
    val configFilepath: String
) {
    val channels = mutableListOf<Channel>()
    private val botCredential = OAuth2Credential("twitch", "oauth:${configuration.botAccountOauth}")
    val webhookSecret = configuration.webhookSecret

    val appCredential = TwitchAppCredential.of(
        configuration.clientId,
        configuration.secretKey,
        tokenRepository = { getToken -> AuthTokenRepository("${configFilepath}\\auth\\bot.json", getToken) }
    )

    val twitchApi = AppTwitchClient.build(appCredential, webhookSecret)

    val client = TwitchClientBuilder.builder()
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
}
