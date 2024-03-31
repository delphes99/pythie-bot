package fr.delphes.overlay.webserver

import fr.delphes.bot.Bot
import fr.delphes.overlay.event.outgoing.Alert
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.util.Collections

fun Application.AlertModule(alerts: Channel<Alert>, bot: Bot) {
    install(WebSockets)

    routing {
        val wsConnections = Collections.synchronizedSet(LinkedHashSet<DefaultWebSocketSession>())

        this@AlertModule.launch {
            for (alert in alerts) {
                wsConnections
                    .map(DefaultWebSocketSession::outgoing)
                    .forEach { connection ->
                        connection.send(
                            Frame.Text(
                                bot.serializer.encodeToString(
                                    SerializableAlert(
                                        alert
                                    )
                                )
                            )
                        )
                    }
            }
        }

        webSocket("/") {
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

                        else -> { /* nothin */
                        }
                    }
                }
            } finally {
                wsConnections -= this
            }
        }
    }
}

@Serializable
private data class SerializableAlert(
    val type: String,
    val parameters: Map<String, String>,
) {
    constructor(alert: Alert) : this(alert.alertType, alert.parameters)
}