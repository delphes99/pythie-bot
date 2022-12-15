package fr.delphes.features.twitch

import fr.delphes.bot.OutgoingEventProcessor
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent

class TwitchEventParameters<T: TwitchIncomingEvent>(
    private val outgoingEventProcessor: OutgoingEventProcessor,
    val event: T
) {
    suspend fun executeOutgoingEvent(event: OutgoingEvent) {
        outgoingEventProcessor.processOutgoingEvent(event)
    }
}