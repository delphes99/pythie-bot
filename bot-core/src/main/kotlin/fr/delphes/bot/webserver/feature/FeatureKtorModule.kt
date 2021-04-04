package fr.delphes.bot.webserver.feature

import fr.delphes.bot.Bot
import fr.delphes.feature.Feature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.serialization.encodeToString

fun Application.Features(bot: Bot) {
    routing {
        get("/features") {
            val json = bot.serializer.encodeToString(bot.features.map(Feature<*>::description))
            this.call.respondText(json, ContentType.Application.Json)
        }
    }
}