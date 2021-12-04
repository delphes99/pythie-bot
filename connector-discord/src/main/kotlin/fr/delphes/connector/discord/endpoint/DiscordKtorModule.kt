package fr.delphes.connector.discord.endpoint

import fr.delphes.connector.discord.DiscordConfiguration
import fr.delphes.connector.discord.DiscordConnector
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.Serializable

fun Application.DiscordModule(discord: DiscordConnector) {
    routing {
        post("/discord/configuration") {
            val configuration = this.call.receive<DiscordConfigurationRequest>()
            discord.configure(DiscordConfiguration(configuration.oAuthToken))
            discord.connect()

            this.context.respond(HttpStatusCode.OK)
        }
        get("/discord/toto") {
            this.context.respond(discord.stateMachine.state.javaClass.toString())
        }
        get("/discord/connect") {
            discord.connect()
            this.context.respond(HttpStatusCode.OK)
        }
        get("/discord/disconnect") {
            discord.disconnect()
            this.context.respond(HttpStatusCode.OK)
        }
    }
}

@Serializable
private data class DiscordConfigurationRequest(val oAuthToken: String)