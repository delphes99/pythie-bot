package fr.delphes.twitch

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.helper.MessageEvent
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport

class IrcClient(
    private val token: String,
    private val channel: String
) {
    private val client = buildClient()
    private val target = "#${channel}"
    private val chan get() = client.getChannel(target).orElseGet { null }

    private fun buildClient(): Client {
        val client = Client
            .builder()
            .nick("pythiebot")
            .server()
            .host("irc.twitch.tv")
            .password("oauth:$token")
            .then()
            .build()

        TwitchSupport.addSupport(client);
        client.connect()
        client.eventManager.registerEventListener(JoinHandler())
        client.eventManager.registerEventListener(MessageHander())
        client.eventManager.registerEventListener(MMessageHander())

        client.addChannel("#${channel}")

        return client
    }

    fun sendMessage(message: String) {
        chan?.sendMessage(message)
    }

    inner class JoinHandler {
        @Handler
        fun onJoin(event: ChannelJoinEvent) {
            chan?.sendMessage("Hi ${event.user.nick}!")
        }
    }

    inner class MessageHander {
        @Handler
        fun onMessage(event: ChannelMessageEvent) {
            chan?.sendMessage("Recu chan message : ${event.message} de ${event.actor.nick}")
            if(event.message == "vips") {
                chan?.sendMessage("/vips")
            }
        }
    }

    inner class MMessageHander {
        @Handler
        fun onMessage(event: MessageEvent) {
            chan?.sendMessage("Recu message : ${event.message}")
        }
    }
}