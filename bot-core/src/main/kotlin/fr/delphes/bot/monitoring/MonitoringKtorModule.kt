package fr.delphes.bot.monitoring

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventId
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.Monitoring(bot: Bot) {
    val statisticService = bot.statisticService
    routing {
        get("/monitoring/events") {
            call.respond(statisticService.getEvents().sortedByDescending { it.date })
        }
        post("/monitoring/replay") {
            val event = call.receive<IncomingEventWrapper<IncomingEvent>>()
            val newIncomingEvent = IncomingEventWrapper(
                data = event.data,
                replay = event.id,
                id = IncomingEventId()
            )

            bot.handle(newIncomingEvent)
            call.respond("OK")
        }
    }
}