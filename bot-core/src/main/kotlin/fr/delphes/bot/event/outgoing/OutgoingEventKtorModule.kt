package fr.delphes.bot.event.outgoing

import fr.delphes.bot.Bot
import fr.delphes.feature.OutgoingEventType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.OutgoingEventKtorModule(bot: Bot) {
    routing {
        get("outgoing-events/types") {
            this.context.respond(
                HttpStatusCode.OK, bot.eventsManager.allGoingEventTypes()
            )
        }
        get("outgoing-events/types/{type}") {
            val type = this.call.parameters["type"]
                ?.let { OutgoingEventType(it) }
                ?: return@get this.context.respond(HttpStatusCode.BadRequest, "outgoing event type missing")

            val newInstance = bot.eventsManager.getNewOutgoingEvent(type)
                ?: return@get this.context.respond(HttpStatusCode.BadRequest, "unknown outgoing event type")
            this.context.respond(HttpStatusCode.OK, newInstance.description())
        }
    }
}