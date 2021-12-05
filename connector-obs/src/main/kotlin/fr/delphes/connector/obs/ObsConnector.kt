package fr.delphes.connector.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.connector.state.DisconnectionRequested
import fr.delphes.bot.connector.state.ErrorOccurred
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.business.SourceFilter
import fr.delphes.connector.obs.endpoints.ObsModule
import fr.delphes.connector.obs.incomingEvent.SceneChanged
import fr.delphes.connector.obs.incomingEvent.SourceFilterActivated
import fr.delphes.connector.obs.incomingEvent.SourceFilterDeactivated
import fr.delphes.connector.obs.outgoingEvent.ObsOutgoingEvent
import fr.delphes.obs.Configuration
import fr.delphes.obs.ObsClient
import fr.delphes.obs.ObsListener
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.InternalSerializationApi
import mu.KotlinLogging

@InternalSerializationApi
class ObsConnector(
    val bot: Bot,
    override val configFilepath: String,
) : Connector<ObsConfiguration, ObsRunTime> {
    override val connectorName = "obs"
    private val repository = ObsConfigurationRepository("${configFilepath}\\obs\\configuration.json")
    override val stateMachine = ConnectorStateMachine<ObsConfiguration, ObsRunTime>(
        repository = repository,
        doConnection = { configuration, dispatchTransition ->
            try {
                //TODO move listener build
                val listeners = ObsListener(
                    onSwitchScene = {
                        bot.handleIncomingEvent(SceneChanged(it.sceneName))
                    },
                    onSourceFilterVisibilityChanged = {
                        val filter = SourceFilter(it.sourceName, it.filterName)
                        val event = if (it.filterEnabled) {
                            SourceFilterActivated(filter)
                        } else {
                            SourceFilterDeactivated(filter)
                        }
                        bot.handleIncomingEvent(event)
                    },
                    onError = { message ->
                        LOGGER.error { "Obs client error : $message" }
                        dispatchTransition(ErrorOccurred(configuration, message))
                    },
                    onExit = {
                        dispatchTransition(DisconnectionRequested())
                    }
                )
                val client = ObsClient(configuration.toObsConfiguration(), listeners)
                client.listen()
                ConnectionSuccessful(configuration, ObsRunTime(client))
            } catch (e: Exception) {
                ErrorOccurred(configuration, "Connection error : ${e.message}")
            }
        },
    )

    private fun ObsConfiguration.toObsConfiguration() = Configuration(host, port, password)

    init {
        runBlocking {
            stateMachine.load()
        }
    }

    override suspend fun execute(event: OutgoingEvent) {
        if (event is ObsOutgoingEvent) {
            event.executeOnObs(this)
        }
    }

    override fun internalEndpoints(application: Application) {
        application.ObsModule(this)
    }

    override fun publicEndpoints(application: Application) {}

    suspend fun connected(doStuff: suspend ObsRunTime.() -> Unit) {
        val currentState = stateMachine.state
        if (currentState is Connected) {
            currentState.runtime.doStuff()
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}