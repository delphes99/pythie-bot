package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

interface EventHandler<T : IncomingEvent> {
    fun handle(event: T): List<OutgoingEvent>
}