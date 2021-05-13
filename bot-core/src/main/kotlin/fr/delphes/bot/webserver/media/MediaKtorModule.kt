package fr.delphes.bot.webserver.media

import fr.delphes.bot.Bot
import io.ktor.application.Application
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.routing.routing
import java.io.File

fun Application.MediaModule(bot: Bot) {
    routing {
        static("media") {
            files("${bot.configFilepath}${File.separator}media")
        }
    }
}