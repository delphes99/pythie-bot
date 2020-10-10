package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

fun <T: IncomingEvent> List<EventHandler<T>>.handleEvent(event: T) : List<OutgoingEvent> =
    flatMap { handler -> handler.handle(event) }