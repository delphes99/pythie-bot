package fr.delphes.bot.connector

import fr.delphes.bot.event.outgoing.OutgoingEvent

interface ConnectorStateManager<CONFIGURATION : ConnectorConfiguration> {
    val status: ConnectorStatus

    suspend fun handle(command: ConnectorCommand, configurationManager: ConfigurationManager<CONFIGURATION>)

    suspend fun execute(event: OutgoingEvent)
}