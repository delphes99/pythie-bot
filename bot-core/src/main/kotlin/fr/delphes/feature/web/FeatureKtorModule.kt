package fr.delphes.feature.web

import fr.delphes.bot.Bot
import fr.delphes.dynamicForm.DynamicFormType
import fr.delphes.dynamicForm.http.getDynamicForm
import fr.delphes.rework.feature.Feature
import fr.delphes.rework.feature.FeatureDefinition
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
            this.context.respond(HttpStatusCode.OK, bot.featuresManager.getEditableFeatures().map { feature ->
                FeatureSummaryDto(
                    feature.id,
                    bot.findEntryOf(feature).type
                )
            })
        }
        get("feature/{id}") {
            val id = this.call.parameters["id"]
                ?.let { FeatureId(it) }
                ?: return@get this.context.respond(HttpStatusCode.BadRequest, "feature id missing")
            bot.featuresManager.getEditableFeature(id)
                ?.let { feature ->
                    feature.toFeatureDto(bot)
                }
                ?.let { description -> this.context.respond(HttpStatusCode.OK, description) }
                ?: this.context.respond(HttpStatusCode.NotFound, "feature with id ${id.value} not found")
        }
        put("feature/{id}") {
            val id = this.call.parameters["id"]
                ?.let { FeatureId(it) }
                ?: return@put this.context.respond(HttpStatusCode.BadRequest, "feature id missing")

            val feature = Feature(
                id,
                getDynamicForm<FeatureDefinition>(bot.serializer)
            )

            bot.featuresManager.upsertFeature(feature)
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

            val emptyFeatureDescription = bot.dynamicFormRegistry.newInstanceOf(request.type) as FeatureDefinition?
                ?: return@post this.context.respond(HttpStatusCode.BadRequest, "feature type not found")

            val feature = Feature(
                id,
                emptyFeatureDescription
            )

            bot.featuresManager.upsertFeature(feature)

            this.context.respond(HttpStatusCode.OK, feature.toFeatureDto(bot))
        }
        post("features/reload") {
            bot.featuresManager.loadConfigurableFeatures()
            this.context.respond(HttpStatusCode.OK)
        }
        get("features/types") {
            this.context.respond(
                HttpStatusCode.OK, bot.dynamicFormRegistry.findByTag("feature").map { it.type }
            )
        }
    }
}

private fun Feature.toFeatureDto(
    bot: Bot,
) = FeatureDto(
    id,
    toDynamicForm(bot).description()
)

private fun Bot.findEntryOf(feature: Feature) =
    dynamicFormRegistry.findByClass(feature.definition) ?: error("Feature definition not found")

private fun Feature.toDynamicForm(bot: Bot) =
    bot.dynamicFormRegistry.transform(definition) ?: error("Feature definition not found")

@Serializable
private class CreateFeatureRequest(
    val type: DynamicFormType,
)