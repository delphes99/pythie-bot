package fr.delphes.bot.connector

interface ConfigurationManager<CONFIGURATION : ConnectorConfiguration> {
    var configuration: CONFIGURATION?

    suspend fun configure(configuration: CONFIGURATION)
}