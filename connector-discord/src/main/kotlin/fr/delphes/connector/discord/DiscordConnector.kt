package fr.delphes.connector.discord

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.SimpleConfigurationManager
import fr.delphes.bot.connector.state.Connected
import fr.delphes.connector.discord.endpoint.DiscordModule
import fr.delphes.state.State
import io.ktor.server.application.Application

class DiscordConnector(
    val bot: Bot,
    override val configFilepath: String
) : Connector<DiscordConfiguration, DiscordRunTime> {
    override val connectorName = "Discord"

    override val states = emptyList<State>()

    override val configurationManager = SimpleConfigurationManager(
        DiscordConfigurationRepository("${configFilepath}\\discord\\configuration.json")
    )

    override val connectionManager = DiscordConnectionManager(this)

    override fun internalEndpoints(application: Application) {
        return application.DiscordModule(this)
    }

    override fun publicEndpoints(application: Application) {
    }

    suspend fun connected(doStuff: suspend DiscordRunTime.() -> Unit) {
        val currentState = connectionManager.state
        if (currentState is Connected) {
            currentState.runtime.doStuff()
        }
    }
}