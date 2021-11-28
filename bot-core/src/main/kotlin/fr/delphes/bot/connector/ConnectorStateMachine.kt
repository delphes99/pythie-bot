package fr.delphes.bot.connector

import fr.delphes.bot.connector.state.Configured
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.Connecting
import fr.delphes.bot.connector.state.ConnectorState
import fr.delphes.bot.connector.state.ConnectorTransition
import fr.delphes.bot.connector.state.Disconnecting
import fr.delphes.bot.connector.state.ErrorOccurred
import fr.delphes.bot.connector.state.InError
import fr.delphes.bot.connector.state.NotConfigured
import fr.delphes.utils.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ConnectorStateMachine<CONFIGURATION, RUNTIME>(
    private val repository: Repository<CONFIGURATION>,
    private val doConnection: suspend CoroutineScope.(CONFIGURATION) -> ConnectorTransition<CONFIGURATION, RUNTIME>,
    private val doDisconnect: suspend CoroutineScope.(CONFIGURATION, RUNTIME) -> ConnectorTransition<CONFIGURATION, RUNTIME>,
    var state: ConnectorState<CONFIGURATION, RUNTIME> = NotConfigured()
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    suspend fun handle(transition: ConnectorTransition<out CONFIGURATION, RUNTIME>) {
        val newState = state.handle(transition)
        state = newState
        when (newState) {
            is Configured -> {
                repository.save(newState.configuration)
            }
            is Connecting -> {
                connect(newState)
            }
            is Disconnecting -> {
                disconnect(newState)
            }
            is Connected,
            is InError,
            is NotConfigured -> {
                //Nothing
            }
        }
    }

    private fun connect(newState: Connecting<out CONFIGURATION, RUNTIME>) {
        val configuration = newState.configuration

        launchEffect(configuration) {
            doConnection(configuration)
        }
    }

    private suspend fun disconnect(newState: Disconnecting<CONFIGURATION, RUNTIME>) {
        val configuration = newState.configuration

        launchEffect(configuration) {
            doDisconnect(configuration, newState.runtime)
        }
    }

    private fun launchEffect(
        configuration: CONFIGURATION,
        doEffect: suspend CoroutineScope.() -> ConnectorTransition<CONFIGURATION, RUNTIME>
    ) {
        scope.launch {
            handle(
                try {
                    doEffect()
                } catch (e: Exception) {
                    ErrorOccurred(configuration, "Error has occurred : ${e.message}")
                }
            )
        }
    }

    suspend fun load() {
        state = repository.load()?.let { Configured(it) } ?: NotConfigured()
    }
}