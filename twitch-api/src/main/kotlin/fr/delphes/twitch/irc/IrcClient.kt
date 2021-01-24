package fr.delphes.twitch.irc

import fr.delphes.twitch.irc.handler.GlobalMessageHandler
import fr.delphes.twitch.irc.handler.JoinHandler
import fr.delphes.twitch.irc.handler.MessageHandler
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
        fun builder(token: String) = Builder(token)
    }

    class Builder(
        private val token: String
    ) {
        var onChannelMessage: ((IrcChannel, Message) -> Unit)? = null
        var onJoin: ((IrcChannel, IrcUser) -> Unit)? = null
        var onMessage: ((String) -> Unit)? = null

        fun withOnChannelMessage(listener: ((IrcChannel, Message) -> Unit)?): Builder {
            onChannelMessage = listener
            return this
        }


        fun withOnJoin(listener: ((IrcChannel, IrcUser) -> Unit)?): Builder {
            onJoin = listener
            return this
        }


        fun withOnMessage(listener: ((String) -> Unit)?): Builder {
            onMessage = listener
            return this
        }

        fun build(): IrcClient {
            val client = Client
                .builder()
                .nick("pythiebot")
                .server()
                .host("irc.twitch.tv")
                .password("oauth:$token")
                .then()
                .build()

            TwitchSupport.addSupport(client)

            onJoin?.also { client.eventManager.registerEventListener(JoinHandler(it)) }
            onChannelMessage?.also { client.eventManager.registerEventListener(MessageHandler(it)) }
            onMessage?.also { client.eventManager.registerEventListener(GlobalMessageHandler(it)) }

            return IrcClient(client)
        }
    }
}