package fr.delphes.bot.connector

import fr.delphes.bot.connector.status.ConnectorStatus
import fr.delphes.bot.event.outgoing.OutgoingEvent

interface ConnectionManager<CONFIGURATION : ConnectorConfiguration> {
    val status: ConnectorStatus

    suspend fun dispatchTransition(command: ConnectorCommand)

    suspend fun execute(event: OutgoingEvent)
}