package fr.delphes.bot.connector.state

import fr.delphes.bot.connector.ConnectorConfiguration
import fr.delphes.bot.connector.ConnectorRuntime

sealed interface ConnectorState<CONFIGURATION: ConnectorConfiguration, RUNTIME: ConnectorRuntime> {
    val configuration: CONFIGURATION?

    suspend fun handle(
        transition: ConnectorTransition<out CONFIGURATION, RUNTIME>
    ): ConnectorState<CONFIGURATION, RUNTIME>
}

internal fun <CONFIGURATION: ConnectorConfiguration, RUNTIME: ConnectorRuntime> ConnectorState<CONFIGURATION, RUNTIME>.configureIfNewConfiguration(newConfiguration: CONFIGURATION): ConnectorState<CONFIGURATION, RUNTIME> {
    return if (newConfiguration == this.configuration) {
        this
    } else {
        Disconnected(newConfiguration)
    }
}

internal fun <CONFIGURATION: ConnectorConfiguration, RUNTIME: ConnectorRuntime> ConnectorState<CONFIGURATION, RUNTIME>.errorMessageFor(error: ErrorOccurred<out CONFIGURATION, RUNTIME>) =
    if (this.configuration == error.configuration) {
        error.error
    } else {
        "error for another configuration"
    }