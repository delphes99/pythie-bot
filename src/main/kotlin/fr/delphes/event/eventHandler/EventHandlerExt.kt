package fr.delphes.event.eventHandler

import fr.delphes.event.incoming.IncomingEvent
import fr.delphes.event.outgoing.OutgoingEvent

fun <T: IncomingEvent> List<EventHandler<T>>.handleEvent(event: T) : List<OutgoingEvent> =
    flatMap { handler -> handler.handle(event) }