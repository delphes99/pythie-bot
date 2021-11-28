package fr.delphes.bot.connector

import fr.delphes.bot.connector.state.Configure
import fr.delphes.bot.connector.state.ConnectionRequested
import fr.delphes.bot.connector.state.DisconnectionRequested

interface ConnectorWithStateMachine<CONFIGURATION, RUNTIME> {
    val stateMachine: ConnectorStateMachine<CONFIGURATION, RUNTIME>

    suspend fun connect() {
        stateMachine.handle(ConnectionRequested())
    }

    suspend fun disconnect() {
        stateMachine.handle(DisconnectionRequested())
    }

    suspend fun configure(configuration: CONFIGURATION) {
        stateMachine.handle(Configure(configuration))
    }
}
