package fr.delphes.connector.discord.endpoint

import fr.delphes.connector.discord.DiscordConfiguration
import fr.delphes.connector.discord.DiscordConnector
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

fun Application.DiscordModule(discord: DiscordConnector) {
    routing {
        post("/discord/configuration") {
            val configuration = this.call.receive<DiscordConfigurationRequest>()
            discord.configure(DiscordConfiguration(configuration.oAuthToken))
            discord.connect()

            this.context.respond(HttpStatusCode.OK)
        }
    }
}

@Serializable
private data class DiscordConfigurationRequest(val oAuthToken: String)