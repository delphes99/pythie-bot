package fr.delphes.twitch.irc.handler

import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcUser
import fr.delphes.twitch.irc.Message
import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent

class MessageHandler(
    private val listener: (IrcChannel, Message) -> Unit
) {
    @Handler
    fun onMessage(event: ChannelMessageEvent) {
        listener(
            IrcChannel(event.channel.name),
            Message(IrcUser(event.actor.nick), event.message)
        )
    }
}