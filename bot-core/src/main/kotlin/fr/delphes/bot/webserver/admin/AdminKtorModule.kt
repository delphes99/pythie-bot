package fr.delphes.bot.webserver.admin

import fr.delphes.bot.Bot
import fr.delphes.feature.HaveHttp
import io.ktor.server.application.Application
import io.ktor.server.http.content.defaultResource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.routing.routing

fun Application.AdminModule(bot: Bot) {
    routing {
        bot.legacyfeatures
            .filterIsInstance<HaveHttp>()
            .map { feature -> feature.module(bot) }
            .forEach { this@AdminModule.it() }

        static("admin") {
            resources("admin")
            defaultResource("index.html", "admin")
        }
    }
}