package fr.delphes.twitch.irc

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.irc.handler.ChannelMessageHandler
import fr.delphes.twitch.irc.handler.GlobalMessageHandler
import fr.delphes.twitch.irc.handler.JoinHandler
import fr.delphes.twitch.irc.handler.LoginRefusedHandler
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport

class IrcClient(
    builder: Builder,
    private val credentialsManager: CredentialsManager,
    private val channel: TwitchChannel
) {
    private val builder = builder
        .withOnLoginFailed {
            refreshCredentials()
        }

    private val connectedChannels = mutableListOf<IrcChannel>()

    private lateinit var client: Client

    fun connect() {
        LOGGER.debug { "IRC : Connect to ${channel.name}" }
        val token = runBlocking {
            credentialsManager.getChannelToken(channel)
        }

        client = builder.buildClient(token ?: error("Token must be provided for channel ${channel.name}"))
        client.connect()
    }

    private fun refreshCredentials() {
        LOGGER.info { "IRC : Refresh credentials for user ${channel.name}" }
        val newToken = runBlocking {
            credentialsManager.getChannelTokenRefreshed(channel)
        }

        client.shutdown()
        client = builder.buildClient(newToken)
        client.connect()
        connectedChannels.forEach { channel ->
            client.addChannel(channel.ircName)
        }
    }

    fun join(channel: IrcChannel) {
        connectedChannels.add(channel)
        client.addChannel(channel.ircName)
    }

    fun sendMessage(channel: IrcChannel, text: String) {
        client.sendMessage(channel.ircName, text)
    }

    fun disconnect() {
        client.shutdown()
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}

        fun builder(channel: TwitchChannel, credentialsManager: CredentialsManager) =
            Builder(channel, credentialsManager)
    }

    class Builder(
        private val channel: TwitchChannel,
        private val credentialsManager: CredentialsManager
    ) {
        var onChannelMessage: ((IrcChannelMessage) -> Unit)? = null
        var onJoin: ((IrcChannel, IrcUser) -> Unit)? = null
        var onMessage: ((IrcMessage) -> Unit)? = null
        var onLoginFailed: (() -> Unit)? = null

        fun withOnChannelMessage(listener: ((IrcChannelMessage) -> Unit)?): Builder {
            onChannelMessage = listener
            return this
        }

        fun withOnJoin(listener: ((IrcChannel, IrcUser) -> Unit)?): Builder {
            onJoin = listener
            return this
        }

        fun withOnMessage(listener: ((IrcMessage) -> Unit)?): Builder {
            onMessage = listener
            return this
        }

        fun withOnLoginFailed(listener: (() -> Unit)?): Builder {
            onLoginFailed = listener
            return this
        }

        fun build(): IrcClient {
            return IrcClient(this, credentialsManager, channel)
        }

        internal fun buildClient(token: AuthToken): Client {
            val builder = Client
                .builder()
                .nick(channel.normalizeName)
                .server()
                .host("irc.twitch.tv")
                .password("oauth:${token.access_token}")
                .then()

            val client = builder.build()

            TwitchSupport.addSupport(client)

            onJoin?.also { client.eventManager.registerEventListener(JoinHandler(it)) }
            onChannelMessage?.also { client.eventManager.registerEventListener(ChannelMessageHandler(it)) }
            onMessage?.also { client.eventManager.registerEventListener(GlobalMessageHandler(it)) }
            onLoginFailed?.also { client.eventManager.registerEventListener(LoginRefusedHandler(it)) }

            return client
        }
    }
}