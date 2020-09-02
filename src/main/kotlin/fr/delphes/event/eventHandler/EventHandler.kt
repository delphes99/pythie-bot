package fr.delphes.event.eventHandler

import fr.delphes.event.incoming.IncomingEvent
import fr.delphes.event.outgoing.OutgoingEvent

interface EventHandler<T : IncomingEvent> {
    fun handle(event: T): List<OutgoingEvent>
}