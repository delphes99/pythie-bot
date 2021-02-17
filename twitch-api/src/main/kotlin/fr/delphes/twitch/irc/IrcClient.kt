package fr.delphes.twitch.irc

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.irc.handler.ChannelMessageHandler
import fr.delphes.twitch.irc.handler.GlobalMessageHandler
import fr.delphes.twitch.irc.handler.JoinHandler
import kotlinx.coroutines.runBlocking
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport

class IrcClient(
    private val client: Client
) {
    fun connect() {
        client.connect()
    }

    fun join(channel: IrcChannel) {
        client.addChannel(channel.ircName)
    }

    fun sendMessage(channel: IrcChannel, text: String) {
        client.sendMessage(channel.ircName, text)
    }

    companion object {
        fun builder(channel: TwitchChannel, credentialsManager: CredentialsManager) = Builder(channel, credentialsManager)
    }

    class Builder(
        private val channel: TwitchChannel,
        private val credentialsManager: CredentialsManager
    ) {
        var onChannelMessage: ((IrcChannelMessage) -> Unit)? = null
        var onJoin: ((IrcChannel, IrcUser) -> Unit)? = null
        var onMessage: ((IrcMessage) -> Unit)? = null

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

        fun build(): IrcClient {
            //TODO refresh token on error ?
            val token = runBlocking {
                credentialsManager.getChannelToken(channel)
            } ?: error("Token must be provided for channel ${channel.name}")

            val client = Client
                .builder()
                .nick(channel.name.toLowerCase())
                .server()
                .host("irc.twitch.tv")
                .password("oauth:${token.access_token}")

                .then().build()

            TwitchSupport.addSupport(client)

            onJoin?.also { client.eventManager.registerEventListener(JoinHandler(it)) }
            onChannelMessage?.also { client.eventManager.registerEventListener(ChannelMessageHandler(it)) }
            onMessage?.also { client.eventManager.registerEventListener(GlobalMessageHandler(it)) }

            return IrcClient(client)
        }
    }
}