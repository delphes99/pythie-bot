package fr.delphes.connector.discord

import fr.delphes.bot.Bot
import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorInternalIncomingEventHandler
import fr.delphes.bot.connector.ConnectorState
import fr.delphes.bot.connector.ConnectorType
import fr.delphes.bot.connector.SimpleConfigurationManager
import fr.delphes.bot.connector.connectionstate.Connected
import fr.delphes.connector.discord.endpoint.DiscordModule
import io.ktor.server.application.Application

class DiscordConnector(
    val bot: Bot,
    override val botConfiguration: BotConfiguration,
) : Connector<DiscordConfiguration, DiscordRunTime> {
    override val connectorType = ConnectorType("Discord")
    override val states = emptyList<ConnectorState>()
    override val configurationManager = SimpleConfigurationManager(
        DiscordConfigurationRepository(botConfiguration.pathOf("discord", "configuration.json"))
    )
    override val connectionManager = DiscordConnectionManager(this)
    override val internalHandlers: List<ConnectorInternalIncomingEventHandler> = emptyList()

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