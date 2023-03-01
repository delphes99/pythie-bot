package fr.delphes.bot.connector.connectionstate

import fr.delphes.bot.connector.ConnectorConfiguration
import fr.delphes.bot.connector.ConnectorRuntime

data class Connecting<CONFIGURATION: ConnectorConfiguration, RUNTIME: ConnectorRuntime>(
    override val configuration: CONFIGURATION
) : ConnectorState<CONFIGURATION, RUNTIME> {
    override suspend fun handle(transition: ConnectorTransition<out CONFIGURATION, RUNTIME>): ConnectorState<CONFIGURATION, RUNTIME> {
        return when (transition) {
            is Configure -> configureIfNewConfiguration(transition.configuration)
            is ConnectionRequested -> this
            is ConnectionSuccessful -> if (configuration == transition.configuration) {
                Connected(configuration, transition.runtime)
            } else {
                InError(configuration, "connected with different configuration")
            }
            is DisconnectionRequested -> Disconnected(configuration)
            is DisconnectionSuccessful -> TODO()
            is ErrorOccurred -> InError(configuration, errorMessageFor(transition))
        }
    }
}