package fr.delphes.bot.webserver.feature

import fr.delphes.bot.Bot
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.Features(bot: Bot) {
    routing {
        get("/features") {
            this.call.respond(bot.features.map { feature -> feature.javaClass.simpleName })
        }
    }
}