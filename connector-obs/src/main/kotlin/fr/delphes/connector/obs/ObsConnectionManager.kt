package fr.delphes.connector.obs

import fr.delphes.bot.connector.StandAloneConnectionManager
import fr.delphes.bot.connector.connectionstate.ConnectionSuccessful
import fr.delphes.bot.connector.connectionstate.ConnectorTransition
import fr.delphes.bot.connector.connectionstate.DisconnectionRequested
import fr.delphes.bot.connector.connectionstate.ErrorOccurred
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.business.SourceFilter
import fr.delphes.connector.obs.incomingEvent.SceneChanged
import fr.delphes.connector.obs.incomingEvent.SourceFilterActivated
import fr.delphes.connector.obs.incomingEvent.SourceFilterDeactivated
import fr.delphes.connector.obs.outgoingEvent.ObsOutgoingEvent
import fr.delphes.obs.Configuration
import fr.delphes.obs.ObsClient
import fr.delphes.obs.ObsListener
import kotlinx.serialization.InternalSerializationApi
import mu.KotlinLogging

@OptIn(InternalSerializationApi::class)
class ObsConnectionManager(
    private val connector: ObsConnector
) : StandAloneConnectionManager<ObsConfiguration, ObsRunTime>(connector.configurationManager) {
    override val connectionName = "OBS Websocket"

    override suspend fun doConnection(configuration: ObsConfiguration): ConnectorTransition<ObsConfiguration, ObsRunTime> {
        return try {
            //TODO move listener build
            val listeners = ObsListener(
                onSwitchScene = {
                    connector.bot.handle(SceneChanged(it.eventData.sceneName))
                },
                onSourceFilterEnableStateChanged = {
                    val filter = SourceFilter(it.eventData.sourceName, it.eventData.filterName)
                    val event = if (it.eventData.filterEnabled) {
                        SourceFilterActivated(filter)
                    } else {
                        SourceFilterDeactivated(filter)
                    }
                    connector.bot.handle(event)
                },
                onError = { message ->
                    LOGGER.error { "Obs client error : $message" }
                    dispatchTransition(ErrorOccurred(configuration, message))
                },
                onExit = {
                    dispatchTransition(DisconnectionRequested())
                }
            )
            val client = ObsClient(configuration.toObsConfiguration(), listeners, connector.bot.serializer)
            client.listen()
            ConnectionSuccessful(configuration, ObsRunTime(client))
        } catch (e: Exception) {
            ErrorOccurred(configuration, "Connection error : ${e.message}")
        }
    }

    private fun ObsConfiguration.toObsConfiguration() = Configuration(host, port, password)

    override suspend fun execute(event: OutgoingEvent) {
        if (event is ObsOutgoingEvent) {
            event.executeOnObs(connector)
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}