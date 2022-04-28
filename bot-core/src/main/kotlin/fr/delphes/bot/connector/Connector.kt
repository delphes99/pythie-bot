package fr.delphes.bot.connector

import fr.delphes.bot.connector.state.ConnectorTransition
import fr.delphes.bot.connector.state.Disconnected
import fr.delphes.bot.connector.status.ConnectorConnectionName
import fr.delphes.bot.connector.status.ConnectorStatus
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.ktor.server.application.Application
import kotlinx.coroutines.CoroutineScope

interface Connector<CONFIGURATION : ConnectorConfiguration, RUNTIME : ConnectorRuntime> {
    val connectorName: String
    val configurationManager: ConfigurationManager<CONFIGURATION>
    val connectorStateManager: ConnectorStateManager<CONFIGURATION>

    val configuration: CONFIGURATION? get() = configurationManager.configuration
    val status: ConnectorStatus get() = connectorStateManager.status

    suspend fun connect() {
        connectorStateManager.handle(ConnectorCommand.CONNECTION_REQUESTED, configurationManager)
    }

    suspend fun disconnect() {
        connectorStateManager.handle(ConnectorCommand.DISCONNECTION_REQUESTED, configurationManager)
    }

    suspend fun configure(configuration: CONFIGURATION) {
        configurationManager.configure(configuration)
        connectorStateManager.handle(ConnectorCommand.DISCONNECTION_REQUESTED, configurationManager)
    }

    val configFilepath: String

    fun internalEndpoints(application: Application)

    fun publicEndpoints(application: Application)

    suspend fun execute(event: OutgoingEvent) {
        connectorStateManager.execute(event)
    }
}

fun <CONFIGURATION : ConnectorConfiguration, RUNTIME : ConnectorRuntime> initStateMachine(
    connectionName: ConnectorConnectionName,
    doConnection: suspend CoroutineScope.(CONFIGURATION, dispatchTransition: suspend (ConnectorTransition<CONFIGURATION, RUNTIME>) -> Unit) -> ConnectorTransition<CONFIGURATION, RUNTIME>,
    executeEvent: suspend StandAloneConnectorStateMachine<CONFIGURATION, RUNTIME>.(event: OutgoingEvent) -> Unit,
    configurationManager: ConfigurationManager<CONFIGURATION>,
): StandAloneConnectorStateMachine<CONFIGURATION, RUNTIME> {
    return StandAloneConnectorStateMachine(
        connectionName = connectionName,
        doConnection = doConnection,
        executeEvent = executeEvent,
        state = Disconnected(configurationManager.configuration)
    )
}