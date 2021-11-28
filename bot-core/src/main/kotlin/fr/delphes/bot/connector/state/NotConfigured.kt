package fr.delphes.bot.connector.state

class NotConfigured<CONFIGURATION, RUNTIME> : ConnectorState<CONFIGURATION, RUNTIME> {
    override suspend fun handle(
        transition: ConnectorTransition<out CONFIGURATION, RUNTIME>
    ): ConnectorState<CONFIGURATION, RUNTIME> {
        return when (transition) {
            is Configure -> Configured(transition.configuration)
            is ConnectionRequested,
            is DisconnectionRequested,
            is ConnectionSuccessful,
            is DisconnectionSuccessful,
            is ErrorOccurred -> this
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return true
    }

    override fun hashCode(): Int {
        return 0
    }

    override val configuration: CONFIGURATION? = null
}