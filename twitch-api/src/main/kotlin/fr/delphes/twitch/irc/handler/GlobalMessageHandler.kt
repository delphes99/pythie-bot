package fr.delphes.twitch.irc.handler

import fr.delphes.twitch.irc.IrcMessage
import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.event.helper.MessageEvent

class GlobalMessageHandler(
    private val listener: (IrcMessage) -> Unit
) {
    @Handler
    fun onMessage(event: MessageEvent) {
        listener(IrcMessage(event.message))
    }
}