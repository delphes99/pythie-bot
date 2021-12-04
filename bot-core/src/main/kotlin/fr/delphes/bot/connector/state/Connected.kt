package fr.delphes.bot.connector.state

data class Connected<CONFIGURATION, RUNTIME>(
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
                Configured(configuration)
            } else {
                InError(configuration, "disconnection received for another configuration")
            }
            is ErrorOccurred -> {
                InError(configuration, errorMessageFor(transition))
            }
        }
    }
}