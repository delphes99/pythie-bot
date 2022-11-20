package fr.delphes.bot

import fr.delphes.bot.event.incoming.IncomingEvent

interface IncomingEventHandler {
    suspend fun handle(incomingEvent: IncomingEvent)
}