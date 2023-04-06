package fr.delphes.bot.connector

class InMemoryConfigurationManager<T : ConnectorConfiguration>(
    private var _configuration: T? = null
) : ConfigurationManager<T> {

    override suspend fun configure(newConfiguration: T) {
        _configuration = newConfiguration
    }

    override val configuration: T? get() = _configuration
}