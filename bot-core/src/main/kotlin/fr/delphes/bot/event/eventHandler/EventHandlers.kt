package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlin.reflect.KClass

class EventHandlers {
    @PublishedApi
    internal val map = mutableMapOf<KClass<*>, MutableList<EventHandler<*>>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified U : IncomingEvent> getHandlers(): List<EventHandler<U>> {
        return map[U::class]?.let { it as List<EventHandler<U>> } ?: emptyList()
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun handleEvent(event: IncomingEvent, channel: ChannelInfo) : List<OutgoingEvent> {
        return map[event::class]
            ?.map { it as EventHandler<IncomingEvent> }
            ?.let { it.map { handler -> GlobalScope.async { handler.handle(event, channel) }}}
            ?.awaitAll()
            ?.flatten()
            ?: emptyList()
    }

    inline fun <reified U : IncomingEvent> addHandler(handler: EventHandler<U>) {
        map.putIfAbsent(U::class, mutableListOf())
        map[U::class]!!.add(handler)
    }
}