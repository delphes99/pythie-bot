package fr.delphes.features.twitch.newFollow

import fr.delphes.bot.OutgoingEventProcessor
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.incomingEvent.NewFollow

class CustomNewFollowParameters(
    private val outgoingEventProcessor: OutgoingEventProcessor,
    val event: NewFollow
) {
    suspend fun executeOutgoingEvent(event: OutgoingEvent) {
        outgoingEventProcessor.processOutgoingEvent(event)
    }
}