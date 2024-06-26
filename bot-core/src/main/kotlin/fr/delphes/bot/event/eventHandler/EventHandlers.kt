package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class EventHandlers(
    val eventHandlers: Map<KClass<*>, List<EventHandler<*>>>,
) {
    companion object {
        private val LOGGER = KotlinLogging.logger {}

        inline fun <reified U : IncomingEvent> of(noinline action: IncomingEventHandlerAction<U>) =
            builder().addHandler(EventHandler.of(action)).build()

        inline fun <reified U : IncomingEvent> of(handler: EventHandler<U>) =
            builder().addHandler(handler).build()

        fun builder() = Builder()

        class Builder {
            @PublishedApi
            internal val map = mutableMapOf<KClass<*>, MutableList<EventHandler<*>>>()

            inline fun <reified U : IncomingEvent> addHandler(handler: EventHandler<U>): Builder {
                map.putIfAbsent(U::class, mutableListOf())
                map[U::class]!!.add(handler)
                return this
            }

            inline fun <reified U : IncomingEvent> addHandler(noinline handler: IncomingEventHandlerAction<U>): Builder {
                map.putIfAbsent(U::class, mutableListOf())
                map[U::class]!!.add(EventHandler.of(handler))
                return this
            }

            fun <U : IncomingEvent> addHandler(clazz: KClass<U>, handler: EventHandler<U>): Builder {
                map.putIfAbsent(clazz, mutableListOf())
                map[clazz]!!.add(handler)
                return this
            }

            fun build() = EventHandlers(map)
        }
    }


    @Suppress("UNCHECKED_CAST")
    inline fun <reified U : IncomingEvent> getHandlers(): List<EventHandler<U>> {
        return eventHandlers[U::class]?.let { it as List<EventHandler<U>> } ?: emptyList()
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun handleEvent(event: IncomingEventWrapper<out IncomingEvent>, bot: Bot) {
        val incomingEvent = event.data
        return coroutineScope {
            eventHandlers[incomingEvent::class]
                ?.map { it as EventHandler<IncomingEvent> }
                ?.forEach { handler ->
                    launch {
                        try {
                            handler.handle(event as IncomingEventWrapper<IncomingEvent>, bot)
                        } catch (e: Exception) {
                            LOGGER.error(e) { "Skip ${handler::class.simpleName} : Error while handling event" }
                        }
                    }
                }
        }
    }
}