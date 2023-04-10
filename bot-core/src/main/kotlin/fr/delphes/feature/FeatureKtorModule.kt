package fr.delphes.feature

import fr.delphes.bot.Bot
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.FeatureAdminModule(bot: Bot) {
    routing {
        get("features") {
            this.context.respond(HttpStatusCode.OK, bot.featuresManager.getEditableFeature())
        }
        post("features/reload") {
            bot.featuresManager.loadConfigurableFeatures()
            this.context.respond(HttpStatusCode.OK)
        }
    }
}