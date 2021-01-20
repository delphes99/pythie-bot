package fr.delphes.connector.discord

import fr.delphes.bot.ClientBot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.endpoint.DiscordModule
import fr.delphes.connector.discord.outgoingEvent.DiscordOutgoingEvent
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking

class DiscordConnector(
    configFilepath: String
) : Connector {
    private val repository = DiscordConfigurationRepository("${configFilepath}\\discord\\configuration.json")
    var state: DiscordState = DiscordState.Unconfigured

    init {
        runBlocking {
            configure(repository.load().oAuthToken)
        }
    }

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

    override fun internalEndpoints(application: Application, bot: ClientBot) {
        return application.DiscordModule(this, bot)
    }

    override fun publicEndpoints(application: Application, bot: ClientBot) {
    }

    fun connected(doStuff: DiscordState.Connected.() -> Unit) {
        val currentState = state
        if(currentState is DiscordState.Connected) {
            currentState.doStuff()
        }
    }

    suspend fun configure(oAuthToken: String) {
        val newConfiguration = DiscordConfiguration(oAuthToken)
        repository.save(newConfiguration)

        state = DiscordState.Unconfigured.configure(oAuthToken)
    }
}