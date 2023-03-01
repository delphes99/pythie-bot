package fr.delphes.bot.connector.connectionstate

sealed interface ConnectorTransition<CONFIGURATION, RUNTIME>

class Configure<CONFIGURATION, RUNTIME>(
    val configuration: CONFIGURATION
) : ConnectorTransition<CONFIGURATION, RUNTIME>

class ConnectionRequested<CONFIGURATION, RUNTIME> : ConnectorTransition<CONFIGURATION, RUNTIME>

class ConnectionSuccessful<CONFIGURATION, RUNTIME>(
    val configuration: CONFIGURATION,
    val runtime: RUNTIME
) : ConnectorTransition<CONFIGURATION, RUNTIME>

class DisconnectionRequested<CONFIGURATION, RUNTIME> : ConnectorTransition<CONFIGURATION, RUNTIME>

class DisconnectionSuccessful<CONFIGURATION, RUNTIME>(
    val configuration: CONFIGURATION
) : ConnectorTransition<CONFIGURATION, RUNTIME>

class ErrorOccurred<CONFIGURATION, RUNTIME>(
    val configuration: CONFIGURATION,
    val error: String
) : ConnectorTransition<CONFIGURATION, RUNTIME>