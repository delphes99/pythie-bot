package fr.delphes.bot.webserver.admin

import fr.delphes.bot.Bot
import fr.delphes.feature.HaveHttp
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing

fun Application.AdminModule(bot: Bot) {
    routing {
        bot.legacyfeatures
            .filterIsInstance<HaveHttp>()
            .map { feature -> feature.module(bot) }
            .forEach { this@AdminModule.it() }

        staticResources("admin", "admin")
    }
}