package fr.delphes.bot.connector.state

import fr.delphes.utils.Repository

data class InError<CONFIGURATION>(
    override val configuration: CONFIGURATION,
    private val error: String
) : ConnectorState<CONFIGURATION> {
    override suspend fun handle(
        transition: ConnectorTransition<CONFIGURATION>,
        repository: Repository<CONFIGURATION>
    ): ConnectorState<CONFIGURATION> {
        return when(transition) {
            is Configure -> configureIfNewConfiguration(transition.configuration)
            is ConnectionRequested -> Connecting(configuration)
            is ConnectionSuccessful -> if(configuration == transition.configuration) {
                Connected(configuration)
            } else {
                InError(configuration, "connected with different configuration")
            }
            is ErrorOccurred -> if(configuration == transition.configuration) {
                InError(configuration, transition.error)
            } else {
                InError(configuration, "error for another configuration")
            }
        }
    }
}