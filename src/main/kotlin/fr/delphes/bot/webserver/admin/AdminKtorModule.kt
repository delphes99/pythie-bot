package fr.delphes.bot.webserver.admin

import fr.delphes.bot.ClientBot
import fr.delphes.feature.HaveAdmin
import io.ktor.application.Application
import io.ktor.routing.routing

fun Application.AdminModule(bot: ClientBot) {
    routing {
        bot.channels.forEach { channel ->
            channel
                .features
                .filterIsInstance<HaveAdmin>()
                .map { feature -> feature.module(channel) }
                .forEach { it() }
        }
    }
}