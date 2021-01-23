package fr.delphes.webservice

import fr.delphes.TwitchConnector
import fr.delphes.bot.ClientBot
import fr.delphes.bot.util.http.httpClient
import fr.delphes.twitch.auth.AuthToken
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.call.receive
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.Serializable
import mu.KotlinLogging

internal fun Application.ConfigurationModule(connector: TwitchConnector, clientBot: ClientBot) {
    routing {
        get("/twitch/configuration") {
            this.call.respond(ConfigurationOverview(connector.configuration.clientId))
        }
        post("/twitch/configuration/appCredential") {
            val configuration = this.call.receive<AppCredentialRequest>()

            connector.configureAppCredential(configuration.clientId, configuration.clientSecret)

            this.context.respond(HttpStatusCode.OK)
        }
        get("/twitch/configuration/userCredential") {
            val code = this.call.parameters["code"]
            val state = this.call.parameters["state"]

            if(state == "botConfiguration") {
                LOGGER.info { "Bot account credential update" }
                val newBotAuth = (httpClient.post<HttpResponse>("https://id.twitch.tv/oauth2/token") {
                    this.parameter("code", code)
                    this.parameter("client_id", connector.configuration.clientId)
                    this.parameter("client_secret", connector.configuration.clientSecret)
                    this.parameter("grant_type", "authorization_code")
                    this.parameter("redirect_uri", "http://localhost:8080/twitch/configuration/userCredential")
                }.receive<AuthToken>())
                connector.newBotAccountConfiguration(newBotAuth)
            } else {
                LOGGER.error { "Unkown state value" }
            }

            this.context.response.status(HttpStatusCode.OK)
        }
    }
}

@Serializable
private data class ConfigurationOverview(
    val clientId: String
)

@Serializable
private data class AppCredentialRequest(
    val clientId: String,
    val clientSecret: String
)

private val LOGGER = KotlinLogging.logger {}