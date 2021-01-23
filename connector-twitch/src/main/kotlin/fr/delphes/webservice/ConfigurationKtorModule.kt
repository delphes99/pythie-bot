package fr.delphes.webservice

import fr.delphes.TwitchConnector
import fr.delphes.bot.ClientBot
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.Serializable

internal fun Application.ConfigurationModule(connector: TwitchConnector, clientBot: ClientBot) {
    routing {
        post("/twitch/configuration/appCredential") {
            val configuration = this.call.receive<AppCredentialRequest>()

            connector.configureAppCredential(configuration.clientId, configuration.clientSecret)

            this.context.respond(HttpStatusCode.OK)
        }
    }
}

@Serializable
private data class AppCredentialRequest(
    val clientId: String,
    val clientSecret: String
)