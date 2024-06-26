package fr.delphes.connector.twitch.webservice

import fr.delphes.bot.WebServer
import fr.delphes.bot.util.http.httpClient
import fr.delphes.connector.twitch.TwitchConfiguration
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.auth.AuthToken
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

internal fun Application.ConfigurationModule(connector: TwitchConnector) {
    routing {
        get("/twitch/configuration") {
            val configuration = connector.configuration
            this.call.respond(
                ConfigurationOverview(
                    configuration?.clientId ?: "",
                    configuration?.botAccountName?.userName,
                    configuration?.channelsName?.toList() ?: emptyList()
                )
            )
        }
        post("/twitch/configuration/appCredential") {
            val request = this.call.receive<AppCredentialRequest>()

            connector.configurationManager.configureAppCredential(request.clientId, request.clientSecret)

            this.context.respond(HttpStatusCode.OK)
        }
        get("/twitch/configuration/userCredential") {
            val code = this.call.parameters["code"]
            val state = this.call.parameters["state"]

            val configuration = connector.configuration ?: error("should have twitch configuration")
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
                    LOGGER.error { "Unknown state value" }
                }
            }

            call.respondRedirect("${WebServer.FRONT_BASE_URL}/admin/#/twitch", false)
        }
        delete("/twitch/configuration/channel/{channel}") {
            val channelName = this.context.parameters["channel"]
            val responseCode = if (channelName != null) {
                connector.configurationManager.removeChannel(channelName)
                HttpStatusCode.OK
            } else {
                HttpStatusCode.BadRequest
            }

            call.respond(responseCode)
        }
    }
}

private suspend fun getAuthToken(
    code: String?,
    configuration: TwitchConfiguration,
) = (httpClient.post("https://id.twitch.tv/oauth2/token") {
    this.parameter("code", code)
    this.parameter("client_id", configuration.clientId)
    this.parameter("client_secret", configuration.clientSecret)
    this.parameter("grant_type", "authorization_code")
    this.parameter("redirect_uri", "http://localhost:8080/twitch/configuration/userCredential")
}.body<AuthToken>())

@Serializable
private data class ConfigurationOverview(
    val clientId: String,
    val botUsername: String?,
    val channels: List<String>,
)

@Serializable
private data class AppCredentialRequest(
    val clientId: String,
    val clientSecret: String,
)

private val LOGGER = KotlinLogging.logger {}