package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.event.incoming.IncomingEvent
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class EventHandlersTest {
    class EventA : IncomingEvent
    class EventB : IncomingEvent

    @Test
    internal fun `get handler by event type`() {
        val eventHandlers = EventHandlers()
        val handlerA = mockk<EventHandler<EventA>>()
        val handlerB = mockk<EventHandler<EventB>>()
        val handlerBbis = mockk<EventHandler<EventB>>()
        eventHandlers.addHandler(handlerA)
        eventHandlers.addHandler(handlerB)
        eventHandlers.addHandler(handlerBbis)

        eventHandlers.getHandlers<EventA>().shouldContainExactly(handlerA)
        eventHandlers.getHandlers<EventB>().shouldContainExactly(handlerB, handlerBbis)
    }

    @Test
    internal suspend fun `handle event by event type`() {
        val eventHandlers = EventHandlers()
        val handlerA = mockk<EventHandler<EventA>>(relaxed = true)

        eventHandlers.addHandler(handlerA)

        val event = EventA()
        eventHandlers.handleEvent(event, mockk())

        coVerify (exactly = 1) { handlerA.handle(event, any()) }
    }
}