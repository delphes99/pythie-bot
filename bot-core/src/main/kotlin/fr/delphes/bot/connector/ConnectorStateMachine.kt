package fr.delphes.bot.connector

import fr.delphes.bot.connector.state.Configured
import fr.delphes.bot.connector.state.ConnectionRequested
import fr.delphes.bot.connector.state.ConnectorState
import fr.delphes.bot.connector.state.ConnectorTransition
import fr.delphes.bot.connector.state.ErrorOccurred
import fr.delphes.bot.connector.state.NotConfigured
import fr.delphes.utils.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnectorStateMachine<CONFIGURATION>(
    private val repository: Repository<CONFIGURATION>
) {
    var state: ConnectorState<CONFIGURATION> = NotConfigured()
    private val scope = CoroutineScope(Dispatchers.Default)

    suspend fun handle(transition: ConnectorTransition<out CONFIGURATION>) {
        state = state.handle(transition, repository)
    }

    suspend fun connect(doConnection: suspend CoroutineScope.() -> ConnectorTransition<CONFIGURATION>) {
        handle(ConnectionRequested())
        val configuration: CONFIGURATION? = state.configuration
        if (configuration != null) {
            scope.launch {
                val transition = try {
                    doConnection()
                } catch (e: Exception) {
                    ErrorOccurred(configuration, "Error has occurred : ${e.message}")
                }
                handle(transition)
            }
        }
    }

    suspend fun load() {
        state = repository.load()?.let { Configured(it) } ?: NotConfigured()
    }
}