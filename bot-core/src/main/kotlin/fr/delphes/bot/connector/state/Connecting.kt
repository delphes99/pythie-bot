package fr.delphes.bot.connector.state

import fr.delphes.utils.Repository

data class Connecting<CONFIGURATION>(
    override val configuration: CONFIGURATION
) : ConnectorState<CONFIGURATION> {
    override suspend fun handle(transition: ConnectorTransition<CONFIGURATION>, repository: Repository<CONFIGURATION>): ConnectorState<CONFIGURATION> {
        return when(transition) {
            is Configure -> configureIfNewConfiguration(transition.configuration)
            is ConnectionRequested -> this
            is ConnectionSuccessful -> if (configuration == transition.configuration) {
                Connected(configuration)
            } else {
                InError(configuration, "connected with different configuration")
            }
            is ErrorOccurred -> InError(configuration, errorMessageFor(transition))
        }
    }
}