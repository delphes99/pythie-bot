package fr.delphes.bot.connector.state

import fr.delphes.utils.Repository

sealed interface ConnectorState<CONFIGURATION> {
    val configuration: CONFIGURATION?

    suspend fun handle(
        transition: ConnectorTransition<CONFIGURATION>,
        repository: Repository<CONFIGURATION>
    ): ConnectorState<CONFIGURATION>
}

internal fun <T> ConnectorState<T>.configureIfNewConfiguration(newConfiguration: T): ConnectorState<T> {
    return if(newConfiguration == this.configuration) {
        this
    } else {
        Configured(newConfiguration)
    }
}

internal fun <T> ConnectorState<T>.errorMessageFor(error: ErrorOccurred<T>) =
    if (this.configuration == error.configuration) {
        error.error
    } else {
        "error for another configuration"
    }