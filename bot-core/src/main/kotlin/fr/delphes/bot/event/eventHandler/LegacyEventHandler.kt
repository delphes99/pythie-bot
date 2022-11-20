package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

interface LegacyEventHandler<T : IncomingEvent> {
    suspend fun handle(event: T, bot: Bot): List<OutgoingEvent>
}