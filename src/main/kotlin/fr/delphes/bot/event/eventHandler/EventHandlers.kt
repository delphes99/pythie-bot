package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import kotlin.reflect.KClass

class EventHandlers {
    @PublishedApi
    internal val map = mutableMapOf<KClass<*>, MutableList<EventHandler<*>>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified U : IncomingEvent> getHandlers(): List<EventHandler<U>> {
        return map[U::class]?.let { it as List<EventHandler<U>> } ?: emptyList()
    }

    inline fun <reified T: IncomingEvent> handleEvent(event: T) : List<OutgoingEvent> =
        getHandlers<T>().flatMap { handler -> handler.handle(event) }

    inline fun <reified U : IncomingEvent> addHandler(handler: EventHandler<U>) {
        map.putIfAbsent(U::class, mutableListOf())
        map[U::class]!!.add(handler)
    }
}