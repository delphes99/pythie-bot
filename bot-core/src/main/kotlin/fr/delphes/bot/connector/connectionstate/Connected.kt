package fr.delphes.bot.connector.connectionstate

import fr.delphes.bot.connector.ConnectorConfiguration
import fr.delphes.bot.connector.ConnectorRuntime

data class Connected<CONFIGURATION: ConnectorConfiguration, RUNTIME: ConnectorRuntime>(
    override val configuration: CONFIGURATION,
    val runtime: RUNTIME
) : ConnectorState<CONFIGURATION, RUNTIME> {
    override suspend fun handle(transition: ConnectorTransition<out CONFIGURATION, RUNTIME>): ConnectorState<CONFIGURATION, RUNTIME> {
        return when (transition) {
            is Configure -> configureIfNewConfiguration(transition.configuration)
            is ConnectionRequested -> this
            is ConnectionSuccessful -> if (configuration == transition.configuration) {
                this
            } else {
                InError(configuration, "connected with different configuration")
            }
            is DisconnectionRequested -> Disconnecting(configuration, runtime)
            is DisconnectionSuccessful -> if (configuration == transition.configuration) {
                Disconnected(configuration)
            } else {
                InError(configuration, "disconnection received for another configuration")
            }
            is ErrorOccurred -> {
                InError(configuration, errorMessageFor(transition))
            }
        }
    }
}