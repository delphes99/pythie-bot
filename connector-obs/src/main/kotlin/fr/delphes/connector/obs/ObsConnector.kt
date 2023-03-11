package fr.delphes.connector.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorState
import fr.delphes.bot.connector.SimpleConfigurationManager
import fr.delphes.bot.connector.connectionstate.Connected
import fr.delphes.connector.obs.endpoints.ObsModule
import fr.delphes.state.State
import io.ktor.server.application.Application
import kotlinx.serialization.InternalSerializationApi
import mu.KotlinLogging

@InternalSerializationApi
class ObsConnector(
    val bot: Bot,
    override val configFilepath: String,
) : Connector<ObsConfiguration, ObsRunTime> {
    override val connectorName = "Obs"

    override val states = emptyList<ConnectorState>()

    override val configurationManager = SimpleConfigurationManager(
        ObsConfigurationRepository("${configFilepath}\\obs\\configuration.json")
    )

    override val connectionManager = ObsConnectionManager(this)

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