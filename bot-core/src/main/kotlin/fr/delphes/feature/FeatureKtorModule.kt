package fr.delphes.feature

import fr.delphes.bot.Bot
import fr.delphes.rework.feature.FeatureId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.FeatureAdminModule(bot: Bot) {
    routing {
        get("features") {
            this.context.respond(HttpStatusCode.OK, bot.featuresManager.getEditableFeatures())
        }
        get("feature/{id}") {
            val id = this.call.parameters["id"]
                ?.let { FeatureId(it) }
                ?: return@get this.context.respond(HttpStatusCode.BadRequest, "feature id missing")
            bot.featuresManager.getEditableFeature(id)
                ?.description()
                ?.let { description -> this.context.respond(HttpStatusCode.OK, description) }
                ?: this.context.respond(HttpStatusCode.NotFound, "feature with id ${id.value} not found")
        }
        post("feature/{id}") {
            val id = this.call.parameters["id"]
                ?.let { FeatureId(it) }
                ?: return@post this.context.respond(HttpStatusCode.BadRequest, "feature id missing")

            val configuration = this.call.receive<FeatureConfiguration>()

            if (configuration.id != id) {
                return@post this.context.respond(HttpStatusCode.BadRequest, "feature id mismatch")
            }

            bot.featuresManager.upsertFeature(configuration)
            this.context.respond(HttpStatusCode.OK)
        }
        post("features/reload") {
            bot.featuresManager.loadConfigurableFeatures()
            this.context.respond(HttpStatusCode.OK)
        }
        get("outgoing-events/types") {
            this.context.respond(HttpStatusCode.OK, bot.outgoingEventsTypes.keys.map(OutgoingEventType::name))
        }
        get("outgoing-events/types/{type}") {
            val id = this.call.parameters["type"]
                ?.let { OutgoingEventType(it) }
                ?: return@get this.context.respond(HttpStatusCode.BadRequest, "outgoing event type missing")

            val eventBuilder = bot.outgoingEventsTypes[id]
                ?: return@get this.context.respond(HttpStatusCode.BadRequest, "unknown outgoing event type")

            this.context.respond(HttpStatusCode.OK, eventBuilder().description())
        }
    }
}