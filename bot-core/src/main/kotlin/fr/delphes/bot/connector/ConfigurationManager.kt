package fr.delphes.bot.connector

interface ConfigurationManager<CONFIGURATION : ConnectorConfiguration> {
    val configuration: CONFIGURATION?

    suspend fun configure(newConfiguration: CONFIGURATION)
}