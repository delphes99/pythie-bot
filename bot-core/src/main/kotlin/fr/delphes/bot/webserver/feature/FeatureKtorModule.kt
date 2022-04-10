package fr.delphes.bot.webserver.feature

import fr.delphes.bot.Bot
import fr.delphes.feature.featureNew.Feature
import fr.delphes.feature.featureNew.FeatureConfiguration
import fr.delphes.feature.featureNew.FeatureState
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
        get("/features/new") {
            val json = bot.serializer.encodeToString(
                bot
                    .featureHandler
                    .features
                    .values
                    .map(Feature<out FeatureState>::configuration)
                    .map { it.description(bot.serializer) }
            )
            this.call.respondText(json, ContentType.Application.Json)
        }
        get("/features/{featureId}/state") {
            val id = this.call.parameters["featureId"]
            val state = bot
                .featureHandler
                .features
                .filter { it.key == id }
                .map { it.value.runtime.state }
                .firstOrNull()

            if (state == null) {
                this.call.respond(HttpStatusCode.NotFound);
            } else {
                val json = bot.serializer.encodeToString(
                    state
                )
                this.call.respondText(json, ContentType.Application.Json)
            }
        }
        get("/features/reload") {
            bot.reloadFeature()
            this.call.respond(HttpStatusCode.OK)
        }
        post("/feature/edit") {
            val configuration = this.call.receive<FeatureConfiguration<out FeatureState>>()
            bot.editFeature(configuration)
            this.call.respond(HttpStatusCode.OK)
        }
    }
}