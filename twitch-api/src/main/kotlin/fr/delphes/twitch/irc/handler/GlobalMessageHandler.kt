package fr.delphes.twitch.irc.handler

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.event.helper.MessageEvent

class GlobalMessageHandler(
    private val listener: (String) -> Unit
) {
    @Handler
    fun onMessage(event: MessageEvent) {
        listener(event.message)
    }
}