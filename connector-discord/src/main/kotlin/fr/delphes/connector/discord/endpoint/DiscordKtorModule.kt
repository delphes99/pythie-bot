package fr.delphes.connector.discord.endpoint

import fr.delphes.bot.connector.state.Configured
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.Connecting
import fr.delphes.bot.connector.state.ConnectorState
import fr.delphes.bot.connector.state.Disconnecting
import fr.delphes.bot.connector.state.InError
import fr.delphes.bot.connector.state.NotConfigured
import fr.delphes.connector.discord.DiscordConfiguration
import fr.delphes.connector.discord.DiscordConnector
import fr.delphes.connector.discord.DiscordRunTime
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
        get("/status/discord") {
            this.context.respond(discord.stateMachine.state.toStatus())
        }
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

private fun ConnectorState<DiscordConfiguration, DiscordRunTime>.toStatus(): DiscordStatus {
    val status = when (this) {
        is Configured -> DiscordStatusEnum.configured
        is Connected -> DiscordStatusEnum.connected
        is Connecting -> DiscordStatusEnum.connected //TODO
        is Disconnecting -> DiscordStatusEnum.configured //TODO
        is InError -> DiscordStatusEnum.error
        is NotConfigured -> DiscordStatusEnum.unconfigured
    }
    return DiscordStatus(status)
}