package fr.delphes.bot.connector

import fr.delphes.bot.connector.state.Configure
import fr.delphes.bot.connector.state.ConnectionRequested
import fr.delphes.bot.connector.state.ConnectorState
import fr.delphes.bot.connector.state.DisconnectionRequested
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.ktor.application.Application

interface Connector<CONFIGURATION: ConnectorConfiguration, RUNTIME: ConnectorRuntime> {
    val connectorName: String
    val stateMachine: ConnectorStateMachine<CONFIGURATION, RUNTIME>
    val state: ConnectorState<CONFIGURATION, RUNTIME> get() = stateMachine.state

    suspend fun connect() {
        stateMachine.handle(ConnectionRequested())
    }

    suspend fun disconnect() {
        stateMachine.handle(DisconnectionRequested())
    }

    suspend fun configure(configuration: CONFIGURATION) {
        stateMachine.handle(Configure(configuration))
    }

    val configFilepath: String

    fun internalEndpoints(application: Application)

    fun publicEndpoints(application: Application)

    suspend fun execute(event: OutgoingEvent)
}