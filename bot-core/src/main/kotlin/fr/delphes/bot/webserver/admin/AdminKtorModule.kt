package fr.delphes.bot.webserver.admin

import fr.delphes.bot.ClientBot
import fr.delphes.feature.HaveHttp
import io.ktor.application.Application
import io.ktor.routing.routing

fun Application.FeaturesModule(bot: ClientBot) {
    routing {
        bot.channels.forEach { channel ->
            channel
                .features
                .filterIsInstance<HaveHttp>()
                .map { feature -> feature.module(channel) }
                .forEach { it() }
        }
    }
}