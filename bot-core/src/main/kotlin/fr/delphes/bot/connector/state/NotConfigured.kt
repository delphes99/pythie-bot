package fr.delphes.bot.connector.state

import fr.delphes.utils.Repository

class NotConfigured<CONFIGURATION>(

) : ConnectorState<CONFIGURATION> {
    override suspend fun handle(
        transition: ConnectorTransition<CONFIGURATION>,
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

    override val configuration: CONFIGURATION? = null
}