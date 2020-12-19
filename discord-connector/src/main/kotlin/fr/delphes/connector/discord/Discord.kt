package fr.delphes.connector.discord

import fr.delphes.connector.Connector
import fr.delphes.connector.discord.endpoint.DiscordModule
import io.ktor.application.Application

class Discord(
    var state: DiscordState = DiscordState.Unconfigured
) : Connector {
    fun connect() {
        val newState = when(val oldState = state) {
            DiscordState.Unconfigured -> oldState
            is DiscordState.Configured -> oldState.connect()
            is DiscordState.Error -> oldState
            is DiscordState.Connected -> oldState
        }

        state = newState
    }

    override fun endpoints(application: Application) {
        return application.DiscordModule(this@Discord)
    }

    fun connected(doStuff: DiscordState.Connected.() -> Unit) {
        val currentState = state
        if(currentState is DiscordState.Connected) {
            currentState.doStuff()
        }
    }
}