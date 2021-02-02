package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.reflect.KClass

class EventHandlers {
    @PublishedApi
    internal val map = mutableMapOf<KClass<*>, MutableList<EventHandler<*>>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified U : IncomingEvent> getHandlers(): List<EventHandler<U>> {
        return map[U::class]?.let { it as List<EventHandler<U>> } ?: emptyList()
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun handleEvent(event: IncomingEvent, bot: ClientBot) : List<OutgoingEvent> {
        return coroutineScope {
            map[event::class]
                ?.map { it as EventHandler<IncomingEvent> }
                ?.let { it.map { handler -> async { handler.handle(event, bot) } } }
                ?.awaitAll()
                ?.flatten()
                ?: emptyList()
        }
    }

    inline fun <reified U : IncomingEvent> addHandler(handler: EventHandler<U>) {
        map.putIfAbsent(U::class, mutableListOf())
        map[U::class]!!.add(handler)
    }
}