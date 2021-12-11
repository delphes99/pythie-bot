package fr.delphes.bot.connector.state

import fr.delphes.bot.connector.ConnectorConfiguration
import fr.delphes.bot.connector.ConnectorRuntime

data class InError<CONFIGURATION: ConnectorConfiguration, RUNTIME: ConnectorRuntime>(
    override val configuration: CONFIGURATION,
    private val error: String
) : ConnectorState<CONFIGURATION, RUNTIME> {
    override suspend fun handle(
        transition: ConnectorTransition<out CONFIGURATION, RUNTIME>
    ): ConnectorState<CONFIGURATION, RUNTIME> {
        return when (transition) {
            is Configure -> configureIfNewConfiguration(transition.configuration)
            is ConnectionRequested -> Connecting(configuration)
            is ConnectionSuccessful -> if (configuration == transition.configuration) {
                Connected(configuration, transition.runtime)
            } else {
                InError(configuration, "connected with different configuration")
            }
            is DisconnectionRequested -> this
            is DisconnectionSuccessful -> if (configuration == transition.configuration) {
                Disconnected(configuration)
            } else {
                InError(configuration, "disconnection received for another configuration")
            }
            is ErrorOccurred -> if (configuration == transition.configuration) {
                InError(configuration, transition.error)
            } else {
                InError(configuration, "error for another configuration")
            }
        }
    }
}