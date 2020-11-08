package fr.delphes.bot.webserver.alert

import fr.delphes.bot.ClientBot
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.launch
import java.util.Collections
import kotlin.collections.LinkedHashSet

fun Application.AlertModule(bot: ClientBot) {
    routing {
        install(WebSockets)
        val wsConnections = Collections.synchronizedSet(LinkedHashSet<DefaultWebSocketSession>())

        bot.channels.forEach { channel ->
            launch {
                for (alert in channel.alerts) {
                    wsConnections
                        .map(DefaultWebSocketSession::outgoing)
                        .forEach { connection -> connection.send(Frame.Text(alert.text)) }
                }
            }

            webSocket("/${channel.name}") {
                wsConnections += this
                try {
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                val text = frame.readText()
                                outgoing.send(Frame.Text("YOU SAID: $text"))
                                if (text.equals("bye", ignoreCase = true)) {
                                    close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                                }
                            }
                        }
                    }
                } finally {
                    wsConnections -= this
                }
            }
        }
    }
}