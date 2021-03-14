package fr.delphes.connector.discord

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.endpoint.DiscordModule
import fr.delphes.connector.discord.outgoingEvent.DiscordOutgoingEvent
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking

class DiscordConnector(
    val bot: Bot,
    override val configFilepath: String
) : Connector {
    private val repository = DiscordConfigurationRepository("${configFilepath}\\discord\\configuration.json")
    var state: DiscordState = DiscordState.Unconfigured

    init {
        runBlocking {
            configure(repository.load().oAuthToken)
        }
    }

    override fun init() {
    }

    override fun connect() {
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

    override suspend fun resetWebhook() {
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

    suspend fun configure(oAuthToken: String) {
        val newConfiguration = DiscordConfiguration(oAuthToken)
        repository.save(newConfiguration)

        state = DiscordState.Unconfigured.configure(oAuthToken, this)
    }
}