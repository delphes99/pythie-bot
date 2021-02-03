package fr.delphes.twitch.irc.handler

import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcUser
import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent

class JoinHandler(
    private val listener: (IrcChannel, IrcUser) -> Unit
) {
    @Handler
    fun onJoin(event: ChannelJoinEvent) {
        listener(IrcChannel.withName(event.channel.name), IrcUser(event.user.nick))
    }
}