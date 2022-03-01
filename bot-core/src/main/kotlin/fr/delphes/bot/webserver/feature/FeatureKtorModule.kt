package fr.delphes.bot.webserver.feature

import fr.delphes.bot.Bot
import fr.delphes.feature.Feature
import fr.delphes.feature.featureNew.FeatureConfiguration
import fr.delphes.feature.featureNew.FeatureHandler
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.encodeToString

fun Application.Features(bot: Bot) {
    routing {
        get("/features/legacy") {
            val json = bot.serializer.encodeToString(bot.legacyfeatures.map(Feature<*>::description) + bot.editableFeatures.map(Feature<*>::description))
            this.call.respondText(json, ContentType.Application.Json)
        }
        get("/features/new") {
            val json = bot.serializer.encodeToString(
                bot
                    .featureHandler
                    .features
                    .values
                    .map(FeatureHandler.Feature::configuration)
            )
            this.call.respondText(json, ContentType.Application.Json)
        }
        get("/features/reload") {
            bot.reloadFeature()
            this.call.respond(HttpStatusCode.OK)
        }
        post("/feature/edit") {
            val configuration = this.call.receive<FeatureConfiguration>()
            bot.editFeature(configuration)
            this.call.respond(HttpStatusCode.OK)
        }
    }
}