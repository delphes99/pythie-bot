package fr.delphes.connector.discord.endpoint

import fr.delphes.connector.discord.DiscordConnector
import fr.delphes.connector.discord.DiscordState
import io.ktor.application.Application
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.response.respond

fun Application.DiscordModule(discord: DiscordConnector) {
    routing {
        get("/status/discord") {
            this.context.respond(discord.state.toStatus())
        }
    }
}

private fun DiscordState.toStatus(): DiscordStatus {
    val status = when (this) {
        DiscordState.Unconfigured -> DiscordStatusEnum.unconfigured
        is DiscordState.Configured -> DiscordStatusEnum.configured
        is DiscordState.Error -> DiscordStatusEnum.error
        is DiscordState.Connected -> DiscordStatusEnum.connected
    }
    return DiscordStatus(status)
}