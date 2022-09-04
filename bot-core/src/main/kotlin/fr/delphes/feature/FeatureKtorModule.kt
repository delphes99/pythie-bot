package fr.delphes.feature

import fr.delphes.bot.Bot
import fr.delphes.feature.featureNew.FeatureConfiguration
import fr.delphes.feature.featureNew.FeatureCreation
import fr.delphes.feature.featureNew.FeatureState
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.encodeToString

fun Application.Features(bot: Bot) {
    routing {
        get("/features") {
            val json = bot.serializer.encodeToString(
                bot.featuresManager.getDescriptors())
            this.call.respondText(json, ContentType.Application.Json)
        }
        get("/features/new") {
            val json = bot.serializer.encodeToString(
                bot.loadFeatures()
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
        post("/feature/create") {
            val configuration = this.call.receive<FeatureCreation>()
            val featureCreated = bot.createFeature(configuration)
            this.call.respond(
                if (featureCreated) {
                    HttpStatusCode.OK
                } else {
                    HttpStatusCode.InternalServerError
                }
            )
        }
        get("/feature/outgoingEventTypes") {
            this.call.respond(HttpStatusCode.OK, bot.outgoingEventRegistry.getRegisteredTypes())
        }
    }
}