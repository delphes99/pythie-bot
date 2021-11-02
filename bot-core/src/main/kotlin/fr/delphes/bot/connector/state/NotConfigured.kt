package fr.delphes.bot.connector.state

import fr.delphes.utils.Repository

class NotConfigured<CONFIGURATION> : ConnectorState<CONFIGURATION> {
    override suspend fun handle(
        transition: ConnectorTransition<out CONFIGURATION>,
        repository: Repository<CONFIGURATION>
    ): ConnectorState<CONFIGURATION> {
        return when (transition) {
            is Configure -> {
                repository.save(transition.configuration)

                Configured(transition.configuration)
            }
            is ConnectionRequested,
            is ConnectionSuccessful,
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