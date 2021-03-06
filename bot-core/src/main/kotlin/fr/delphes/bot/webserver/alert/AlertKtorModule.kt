package fr.delphes.bot.webserver.alert

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.utils.serialization.Serializer
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.util.*
import kotlin.collections.LinkedHashSet

fun Application.AlertModule(bot: Bot) {
    routing {
        install(WebSockets)
        val wsConnections = Collections.synchronizedSet(LinkedHashSet<DefaultWebSocketSession>())

        launch {
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