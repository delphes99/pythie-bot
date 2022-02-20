package fr.delphes.bot.webserver.admin

import fr.delphes.bot.Bot
import fr.delphes.feature.HaveHttp
import io.ktor.application.Application
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.routing

fun Application.AdminModule(bot: Bot) {
    routing {
        bot.legacyfeatures
            .filterIsInstance<HaveHttp>()
            .map { feature -> feature.module(bot) }
            .forEach { it() }

        static("admin") {
            resources("admin")
            defaultResource("index.html", "admin")
        }
    }
}