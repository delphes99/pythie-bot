package fr.delphes.connector.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorInternalIncomingEventHandler
import fr.delphes.bot.connector.ConnectorState
import fr.delphes.bot.connector.ConnectorType
import fr.delphes.bot.connector.SimpleConfigurationManager
import fr.delphes.bot.connector.connectionstate.Connected
import fr.delphes.connector.obs.endpoints.ObsModule
import fr.delphes.state.enumeration.EnumerationState
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.Application

class ObsConnector(
    val bot: Bot,
    override val botConfiguration: BotConfiguration,
) : Connector<ObsConfiguration, ObsRunTime> {
    override val connectorType = ConnectorType("Obs")

    override val states = emptyList<ConnectorState>()
    override val enumerationStates = emptyList<EnumerationState<*>>()

    override val configurationManager = SimpleConfigurationManager(
        ObsConfigurationRepository(botConfiguration.pathOf("obs", "configuration.json"))
    )

    override val connectionManager = ObsConnectionManager(this)
    override val internalHandlers: List<ConnectorInternalIncomingEventHandler> = emptyList()

    override fun internalEndpoints(application: Application) {
        application.ObsModule(this)
    }

    override fun publicEndpoints(application: Application) {}

    suspend fun connected(doStuff: suspend ObsRunTime.() -> Unit) {
        val currentState = connectionManager.state
        if (currentState is Connected) {
            currentState.runtime.doStuff()
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}