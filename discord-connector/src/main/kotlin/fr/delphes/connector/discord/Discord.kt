package fr.delphes.connector.discord

import fr.delphes.connector.discord.endpoint.DiscordModule
import io.ktor.application.Application

class Discord {
    var state: DiscordState = DiscordState.Unconfigured

    fun connect() {
        //TODO lock
        val newState = when(val oldState = state) {
            DiscordState.Unconfigured -> oldState
            is DiscordState.Configured -> oldState.connect()
            is DiscordState.Error -> oldState
            is DiscordState.Connected -> oldState
        }

        state = newState
    }

    fun endpoint(application: Application) {
        return application.DiscordModule(this@Discord)
    }
}