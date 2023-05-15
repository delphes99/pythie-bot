package fr.delphes.feature

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.OutgoingEventBuilderDefinition
import fr.delphes.rework.feature.FeatureId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

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

        put("feature/{id}") {
            val id = this.call.parameters["id"]
                ?.let { FeatureId(it) }
                ?: return@put this.context.respond(HttpStatusCode.BadRequest, "feature id missing")

            val configuration = this.call.receive<FeatureConfiguration>()

            if (configuration.id != id) {
                return@put this.context.respond(HttpStatusCode.BadRequest, "feature id mismatch")
            }

            bot.featuresManager.upsertFeature(configuration)
            this.context.respond(HttpStatusCode.OK)
        }
        post("feature/{id}") {
            val id = this.call.parameters["id"]
                ?.let { FeatureId(it) }
                ?: return@post this.context.respond(HttpStatusCode.BadRequest, "feature id missing")

            if (bot.featuresManager.getEditableFeature(id) != null) {
                return@post this.context.respond(HttpStatusCode.Conflict, "feature id already exists")
            }

            val request = this.call.receive<CreateFeatureRequest>()
            val definition = bot.featureConfigurationsType
                .firstOrNull { it.type == request.type }
                ?: return@post this.context.respond(HttpStatusCode.BadRequest, "feature type not found")

            val newConfiguration = definition.provideNewConfiguration(id)
            bot.featuresManager.upsertFeature(newConfiguration)
            this.context.respond(HttpStatusCode.OK, newConfiguration)
        }
        post("features/reload") {
            bot.featuresManager.loadConfigurableFeatures()
            this.context.respond(HttpStatusCode.OK)
        }
        get("outgoing-events/types") {
            this.context.respond(
                HttpStatusCode.OK, bot.outgoingEventsTypes
                    .map(OutgoingEventBuilderDefinition::type)
                    .map(OutgoingEventType::name)
            )
        }
        get("features/types") {
            this.context.respond(
                HttpStatusCode.OK, bot.featureConfigurationsType
                    .map(FeatureConfigurationBuilderRegistry::type)
            )
        }
        get("outgoing-events/types/{type}") {
            val id = this.call.parameters["type"]
                ?.let { OutgoingEventType(it) }
                ?: return@get this.context.respond(HttpStatusCode.BadRequest, "outgoing event type missing")

            val definition = bot.outgoingEventsTypes
                .firstOrNull { it.type == id }
                ?: return@get this.context.respond(HttpStatusCode.BadRequest, "unknown outgoing event type")

            this.context.respond(HttpStatusCode.OK, definition.providerEmptyDescription())
        }
    }
}

@Serializable
private class CreateFeatureRequest(
    val type: FeatureConfigurationType,
)