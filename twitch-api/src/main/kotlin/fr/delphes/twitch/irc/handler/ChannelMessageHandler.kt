package fr.delphes.twitch.irc.handler

import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcUser
import fr.delphes.twitch.irc.IrcChannelMessage
import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent

class ChannelMessageHandler(
    private val listener: (IrcChannelMessage) -> Unit
) {
    @Handler
    fun onMessage(event: ChannelMessageEvent) {
        listener(
            IrcChannelMessage(
                IrcChannel.withName(event.channel.name),
                IrcUser(event.actor.nick),
                event.message
            )
        )
    }
}