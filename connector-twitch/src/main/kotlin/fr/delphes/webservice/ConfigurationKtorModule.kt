package fr.delphes.webservice

import fr.delphes.ConfigurationTwitchAccount
import fr.delphes.TwitchConfiguration
import fr.delphes.TwitchConnector
import fr.delphes.bot.WebServer
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
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.Serializable
import mu.KotlinLogging

internal fun Application.ConfigurationModule(connector: TwitchConnector) {
    routing {
        get("/twitch/configuration") {
            val configuration = connector.configuration
            this.call.respond(
                ConfigurationOverview(
                    configuration.clientId,
                    configuration.botAccountCredential?.userName,
                    configuration.listChannelCredential.map(ConfigurationTwitchAccount::userName)
                )
            )
        }
        post("/twitch/configuration/appCredential") {
            val request = this.call.receive<AppCredentialRequest>()

            connector.configureAppCredential(request.clientId, request.clientSecret)

            this.context.respond(HttpStatusCode.OK)
        }
        get("/twitch/configuration/userCredential") {
            val code = this.call.parameters["code"]
            val state = this.call.parameters["state"]

            val configuration = connector.configuration
            when (state) {
                "botConfiguration" -> {
                    LOGGER.info { "Bot account credential update" }
                    val newBotAuth = getAuthToken(code, configuration)
                    connector.newBotAccountConfiguration(newBotAuth)
                }
                "addChannel" -> {
                    LOGGER.info { "Add channel credential" }
                    val channelAuth = getAuthToken(code, configuration)
                    connector.addChannelConfiguration(channelAuth)
                }
                else -> {
                    LOGGER.error { "Unkown state value" }
                }
            }

            call.respondRedirect("${WebServer.FRONT_BASE_URL}/admin/#/twitch", false)
        }
    }
}

private suspend fun getAuthToken(
    code: String?,
    configuration: TwitchConfiguration
) = (httpClient.post<HttpResponse>("https://id.twitch.tv/oauth2/token") {
    this.parameter("code", code)
    this.parameter("client_id", configuration.clientId)
    this.parameter("client_secret", configuration.clientSecret)
    this.parameter("grant_type", "authorization_code")
    this.parameter("redirect_uri", "http://localhost:8080/twitch/configuration/userCredential")
}.receive<AuthToken>())

@Serializable
private data class ConfigurationOverview(
    val clientId: String,
    val botUsername: String?,
    val channels: List<String>
)

@Serializable
private data class AppCredentialRequest(
    val clientId: String,
    val clientSecret: String
)

private val LOGGER = KotlinLogging.logger {}