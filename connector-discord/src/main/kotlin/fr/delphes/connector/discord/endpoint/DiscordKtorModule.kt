package fr.delphes.connector.discord.endpoint

import fr.delphes.bot.ClientBot
import fr.delphes.connector.discord.DiscordConnector
import fr.delphes.connector.discord.DiscordState
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.Serializable

fun Application.DiscordModule(discord: DiscordConnector, clientBot: ClientBot) {
    routing {
        get("/status/discord") {
            this.context.respond(discord.state.toStatus())
        }
        post("/discord/configuration") {
            val configuration = this.call.receive<DiscordConfigurationRequest>()
            discord.configure(configuration.oAuthToken)
            discord.connect(clientBot)

            this.context.respond(HttpStatusCode.OK)
        }
    }
}

@Serializable
private data class DiscordConfigurationRequest(val oAuthToken: String)

private fun DiscordState.toStatus(): DiscordStatus {
    val status = when (this) {
        DiscordState.Unconfigured -> DiscordStatusEnum.unconfigured
        is DiscordState.Configured -> DiscordStatusEnum.configured
        is DiscordState.Error -> DiscordStatusEnum.error
        is DiscordState.Connected -> DiscordStatusEnum.connected
    }
    return DiscordStatus(status)
}