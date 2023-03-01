package fr.delphes.bot.connector

import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.Connecting
import fr.delphes.bot.connector.state.ConnectionRequested
import fr.delphes.bot.connector.state.ConnectorState
import fr.delphes.bot.connector.state.ConnectorTransition
import fr.delphes.bot.connector.state.Disconnected
import fr.delphes.bot.connector.state.Disconnecting
import fr.delphes.bot.connector.state.DisconnectionRequested
import fr.delphes.bot.connector.state.DisconnectionSuccessful
import fr.delphes.bot.connector.state.ErrorOccurred
import fr.delphes.bot.connector.state.InError
import fr.delphes.bot.connector.status.ConnectorConnectionName
import fr.delphes.bot.connector.status.ConnectorConnectionStatus
import fr.delphes.bot.connector.status.ConnectorStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class StandAloneConnectionManager<CONFIGURATION : ConnectorConfiguration, RUNTIME : ConnectorRuntime>(
    val configurationManager: ConfigurationManager<CONFIGURATION>,
    var state: ConnectorState<CONFIGURATION, RUNTIME> = Disconnected(configurationManager.configuration)
) : ConnectionManager<CONFIGURATION> {
    protected val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    abstract val connectionName: ConnectorConnectionName

    abstract suspend fun doConnection(configuration: CONFIGURATION) : ConnectorTransition<CONFIGURATION, RUNTIME>

    override val status: ConnectorStatus
        get() = ConnectorStatus(
            mapOf(
                connectionName to
                        when (state) {
                            is Connected -> ConnectorConnectionStatus.Connected
                            is Connecting -> ConnectorConnectionStatus.Connecting
                            is Disconnected -> ConnectorConnectionStatus.Configured //TODO
                            is Disconnecting -> ConnectorConnectionStatus.Disconnecting
                            is InError -> ConnectorConnectionStatus.InError
                        }
            )
        )

    override suspend fun dispatchTransition(command: ConnectorCommand) {
        dispatchTransition(toTransition(command))
    }

    private fun toTransition(command: ConnectorCommand): ConnectorTransition<CONFIGURATION, RUNTIME> = when (command) {
        ConnectorCommand.CONNECTION_REQUESTED -> ConnectionRequested()
        ConnectorCommand.DISCONNECTION_REQUESTED -> DisconnectionRequested()
    }

    suspend fun dispatchTransition(transition: ConnectorTransition<out CONFIGURATION, RUNTIME>) {
        val oldState = state
        val newState = oldState.handle(transition)

        if (oldState is Connected && newState != oldState) {
            oldState.runtime.kill()
        }

        state = newState

        when (newState) {
            is Connecting -> {
                connect(newState)
            }
            is Disconnecting -> {
                disconnect(newState)
            }
            is Disconnected,
            is Connected,
            is InError -> {
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
            newState.runtime.kill()
            DisconnectionSuccessful(configuration)
        }
    }

    private fun launchEffect(
        configuration: CONFIGURATION,
        doEffect: suspend CoroutineScope.() -> ConnectorTransition<CONFIGURATION, RUNTIME>
    ) {
        scope.launch {
            dispatchTransition(
                try {
                    doEffect()
                } catch (e: Exception) {
                    ErrorOccurred(configuration, "Error has occurred : ${e.message}")
                }
            )
        }
    }
}