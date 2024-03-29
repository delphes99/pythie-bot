package fr.delphes.feature

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

@Deprecated("migrate to fr.delphes.rework.FeatureDefinition")
interface Feature {
    val eventHandlers: LegacyEventHandlers

    suspend fun handleIncomingEvent(incomingEvent: IncomingEvent, bot: Bot): List<OutgoingEvent> {
        return eventHandlers.handleEvent(incomingEvent, bot)
    }
}