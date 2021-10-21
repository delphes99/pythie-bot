package fr.delphes.bot.connector

import fr.delphes.bot.connector.state.ConnectorState
import fr.delphes.bot.connector.state.ConnectorTransition
import fr.delphes.bot.connector.state.NotConfigured
import fr.delphes.utils.Repository

class ConnectorStateMachine<CONFIGURATION>(
    private val repository: Repository<CONFIGURATION>
) {
    var state: ConnectorState<CONFIGURATION> = NotConfigured()

    suspend fun handle(transition: ConnectorTransition<CONFIGURATION>) {
        state = state.handle(transition, repository)
    }
}