package fr.delphes.features.twitch.command

import fr.delphes.bot.OutgoingEventProcessor
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.incomingEvent.CommandAsked

data class CustomCommandParameters(
    private val outgoingEventProcessor: OutgoingEventProcessor,
    val event: CommandAsked
) {
    suspend fun executeOutgoingEvent(event: OutgoingEvent) {
        outgoingEventProcessor.processOutgoingEvent(event)
    }
}