package fr.delphes.bot.connector.state

sealed interface ConnectorTransition<CONFIGURATION>

class Configure<CONFIGURATION>(
    val configuration: CONFIGURATION
) : ConnectorTransition<CONFIGURATION>

class ConnectionRequested<CONFIGURATION> : ConnectorTransition<CONFIGURATION>

class ConnectionSuccessful<CONFIGURATION>(
    val configuration: CONFIGURATION,
) : ConnectorTransition<CONFIGURATION>

class ErrorOccurred<CONFIGURATION>(
    val configuration: CONFIGURATION,
    val error: String
) : ConnectorTransition<CONFIGURATION>