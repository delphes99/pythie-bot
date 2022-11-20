package fr.delphes.bot

import fr.delphes.bot.event.outgoing.OutgoingEvent

interface OutgoingEventProcessor {
    suspend fun processOutgoingEvent(event: OutgoingEvent)
}