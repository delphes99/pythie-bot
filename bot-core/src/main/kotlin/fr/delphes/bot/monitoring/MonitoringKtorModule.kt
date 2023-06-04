package fr.delphes.bot.monitoring

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
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
            call.respond(statisticService.getEvents())
        }
        post("/monitoring/replay") {
            val event = call.receive<IncomingEvent>()
            bot.handle(event)
            call.respond("OK")
        }
    }
}