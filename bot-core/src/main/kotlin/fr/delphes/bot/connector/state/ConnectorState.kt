package fr.delphes.bot.connector.state

sealed interface ConnectorState<CONFIGURATION, RUNTIME> {
    val configuration: CONFIGURATION?

    suspend fun handle(
        transition: ConnectorTransition<out CONFIGURATION, RUNTIME>
    ): ConnectorState<CONFIGURATION, RUNTIME>
}

internal fun <CONFIGURATION, RUNTIME> ConnectorState<CONFIGURATION, RUNTIME>.configureIfNewConfiguration(newConfiguration: CONFIGURATION): ConnectorState<CONFIGURATION, RUNTIME> {
    return if (newConfiguration == this.configuration) {
        this
    } else {
        Configured(newConfiguration)
    }
}

internal fun <CONFIGURATION, RUNTIME> ConnectorState<CONFIGURATION, RUNTIME>.errorMessageFor(error: ErrorOccurred<out CONFIGURATION, RUNTIME>) =
    if (this.configuration == error.configuration) {
        error.error
    } else {
        "error for another configuration"
    }