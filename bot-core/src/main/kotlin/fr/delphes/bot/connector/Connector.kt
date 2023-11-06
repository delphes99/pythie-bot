package fr.delphes.bot.connector

import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.connector.status.ConnectorStatus
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.ktor.server.application.Application

interface Connector<CONFIGURATION : ConnectorConfiguration, RUNTIME : ConnectorRuntime> {
    val connectorType: ConnectorType
    val states: List<ConnectorState>
    val configurationManager: ConfigurationManager<CONFIGURATION>
    val connectionManager: ConnectionManager<CONFIGURATION>
    val internalHandlers: List<ConnectorInternalIncomingEventHandler>

    val configuration: CONFIGURATION? get() = configurationManager.configuration
    val status: ConnectorStatus get() = connectionManager.status

    suspend fun connect() {
        connectionManager.dispatchTransition(ConnectorCommand.CONNECTION_REQUESTED)
    }

    suspend fun disconnect() {
        connectionManager.dispatchTransition(ConnectorCommand.DISCONNECTION_REQUESTED)
    }

    suspend fun configure(configuration: CONFIGURATION) {
        configurationManager.configure(configuration)
        connectionManager.dispatchTransition(ConnectorCommand.DISCONNECTION_REQUESTED)
    }

    val botConfiguration: BotConfiguration

    fun internalEndpoints(application: Application)

    fun publicEndpoints(application: Application)

    suspend fun execute(event: OutgoingEvent) {
        connectionManager.execute(event)
    }

    suspend fun handleForInternal(event: IncomingEventWrapper<out IncomingEvent>) {
        internalHandlers.forEach { handler ->
            handler.handleIncomingEvent(event)
        }
    }
}