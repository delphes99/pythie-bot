package fr.delphes.bot.connector

interface ConnectorStateManager<CONFIGURATION : ConnectorConfiguration> {
    val status: ConnectorStatus
    suspend fun handle(command: ConnectorCommand, configurationManager: ConfigurationManager<CONFIGURATION>)
}