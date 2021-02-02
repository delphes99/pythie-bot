package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

interface EventHandler<T : IncomingEvent> {
    suspend fun handle(event: T, bot: ClientBot): List<OutgoingEvent>
}