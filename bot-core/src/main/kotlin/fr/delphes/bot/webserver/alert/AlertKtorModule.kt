package fr.delphes.bot.webserver.alert

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.utils.serialization.Serializer
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
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.util.Collections

fun Application.AlertModule(bot: Bot) {
    install(WebSockets)

    routing {
        val wsConnections = Collections.synchronizedSet(LinkedHashSet<DefaultWebSocketSession>())

        this@AlertModule.launch {
            for (alert in bot.alerts) {
                wsConnections
                    .map(DefaultWebSocketSession::outgoing)
                    .forEach { connection -> connection.send(Frame.Text(Serializer.encodeToString(SerializableAlert(alert)))) }
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
                        else -> { /* nothin */}
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
    val parameters: Map<String, String>
) {
    constructor(alert: Alert): this(alert.type, alert.parameters)
}