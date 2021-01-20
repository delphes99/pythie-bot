package fr.delphes.connector.discord

import fr.delphes.bot.ClientBot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.endpoint.DiscordModule
import fr.delphes.connector.discord.outgoingEvent.DiscordOutgoingEvent
import io.ktor.application.Application

class DiscordConnector(
    var state: DiscordState = DiscordState.Unconfigured,
    private val configFilepath: String
) : Connector {
    override fun connect(bot: ClientBot) {
        val newState = when(val oldState = state) {
            DiscordState.Unconfigured -> oldState
            is DiscordState.Configured -> oldState.connect()
            is DiscordState.Error -> oldState
            is DiscordState.Connected -> oldState
        }

        state = newState
    }

    override suspend fun execute(event: OutgoingEvent) {
        if(event is DiscordOutgoingEvent) {
            event.executeOnDiscord(this)
        }
    }

    override fun initChannel(bot: ClientBot) {
    }

    override fun internalEndpoints(application: Application) {
        return application.DiscordModule(this)
    }

    override fun publicEndpoints(application: Application) {
    }

    fun connected(doStuff: DiscordState.Connected.() -> Unit) {
        val currentState = state
        if(currentState is DiscordState.Connected) {
            currentState.doStuff()
        }
    }
}