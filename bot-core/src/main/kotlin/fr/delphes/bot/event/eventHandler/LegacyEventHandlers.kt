package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import mu.KotlinLogging
import kotlin.reflect.KClass

class LegacyEventHandlers(
    val eventHandlers: Map<KClass<*>, List<LegacyEventHandler<*>>>
) {
    companion object {
        private val LOGGER = KotlinLogging.logger {}

        val Empty = LegacyEventHandlers(emptyMap())

        fun builder() = Builder()

        class Builder {
            @PublishedApi
            internal val map = mutableMapOf<KClass<*>, MutableList<LegacyEventHandler<*>>>()

            inline fun <reified U : IncomingEvent> addHandler(handler: LegacyEventHandler<U>): Builder {
                map.putIfAbsent(U::class, mutableListOf())
                map[U::class]!!.add(handler)
                return this
            }

            fun build() = LegacyEventHandlers(map)
        }
    }


    @Suppress("UNCHECKED_CAST")
    inline fun <reified U : IncomingEvent> getHandlers(): List<LegacyEventHandler<U>> {
        return eventHandlers[U::class]?.let { it as List<LegacyEventHandler<U>> } ?: emptyList()
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun handleEvent(event: IncomingEvent, bot: Bot): List<OutgoingEvent> {
        return coroutineScope {
            eventHandlers[event::class]
                ?.map { it as LegacyEventHandler<IncomingEvent> }
                ?.let { handlers ->
                    handlers.map { handler ->
                        async {
                            try {
                                handler.handle(event, bot)
                            } catch (e: Exception) {
                                LOGGER.error(e) { "Skip ${handler::class.simpleName} : Error while handling event" }
                                emptyList()
                            }
                        }
                    }
                }
                ?.awaitAll()
                ?.flatten()
                ?: emptyList()
        }
    }
}