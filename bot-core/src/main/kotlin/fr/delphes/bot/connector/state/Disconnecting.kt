package fr.delphes.bot.connector.state

import fr.delphes.bot.connector.ConnectorConfiguration
import fr.delphes.bot.connector.ConnectorRuntime

data class Disconnecting<CONFIGURATION: ConnectorConfiguration, RUNTIME: ConnectorRuntime>(
    override val configuration: CONFIGURATION,
    val runtime: RUNTIME
) : ConnectorState<CONFIGURATION, RUNTIME> {
    override suspend fun handle(
        transition: ConnectorTransition<out CONFIGURATION, RUNTIME>
    ): ConnectorState<CONFIGURATION, RUNTIME> {
        return when (transition) {
            is Configure -> Disconnected(transition.configuration)
            is ConnectionRequested -> Connected(configuration, runtime)
            is ConnectionSuccessful -> if (configuration == transition.configuration) {
                this
            } else {
                InError(configuration, "error for another configuration")
            }
            is DisconnectionRequested -> this
            is DisconnectionSuccessful -> if (configuration == transition.configuration) {
                Disconnected(configuration)
            } else {
                InError(configuration, "disconnection received for another configuration")
            }
            is ErrorOccurred -> InError(configuration, errorMessageFor(transition))
        }
    }

}