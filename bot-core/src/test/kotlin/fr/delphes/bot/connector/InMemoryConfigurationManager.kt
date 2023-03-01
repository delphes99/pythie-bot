package fr.delphes.bot.connector

class InMemoryConfigurationManager<T : ConnectorConfiguration>(
    override var configuration: T? = null
) : ConfigurationManager<T> {

    override suspend fun configure(configuration: T) {
        this.configuration = configuration
    }
}