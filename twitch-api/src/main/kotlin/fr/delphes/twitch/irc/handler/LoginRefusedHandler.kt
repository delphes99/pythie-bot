package fr.delphes.twitch.irc.handler

import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.event.user.ServerNoticeEvent

class LoginRefusedHandler(
    private val listener: () -> Unit
) {
    @Handler
    fun onJoin(event: ServerNoticeEvent) {
        if(event.message == "Login authentication failed") {
            listener()
        }
    }
}