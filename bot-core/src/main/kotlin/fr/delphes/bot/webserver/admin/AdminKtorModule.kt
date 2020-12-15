package fr.delphes.bot.webserver.admin

import fr.delphes.bot.ClientBot
import fr.delphes.feature.HaveHttp
import io.ktor.application.Application
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.routing

fun Application.AdminModule(bot: ClientBot) {
    routing {
        bot.channels.forEach { channel ->
            channel
                .features
                .filterIsInstance<HaveHttp>()
                .map { feature -> feature.module(channel) }
                .forEach { it() }
        }
        static("/admin") {
            resources("admin")
            defaultResource("index.html", "admin")
        }
    }
}