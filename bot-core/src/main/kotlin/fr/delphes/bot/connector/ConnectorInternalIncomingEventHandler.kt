package fr.delphes.bot.connector

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper

interface ConnectorInternalIncomingEventHandler {
    suspend fun handleIncomingEvent(event: IncomingEventWrapper<out IncomingEvent>)
}