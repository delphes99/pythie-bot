package fr.delphes.bot.monitoring

import fr.delphes.bot.Bot
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.Monitoring(bot: Bot) {
    val statisticService = bot.statisticService
    routing {
        get("/monitoring/events") {
            call.respond(statisticService.getEvents())
        }
    }
}