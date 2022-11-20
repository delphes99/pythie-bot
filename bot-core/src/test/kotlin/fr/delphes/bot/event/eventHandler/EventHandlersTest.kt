package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.event.incoming.IncomingEvent
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class EventHandlersTest {
    class EventA : IncomingEvent
    class EventB : IncomingEvent

    @Test
    internal fun `get handler by event type`() {
        val handlerA = mockk<EventHandler<EventA>>()
        val handlerB = mockk<EventHandler<EventB>>()
        val handlerBbis = mockk<EventHandler<EventB>>()
        val eventHandlers = EventHandlers.builder()
            .addHandler(handlerA)
            .addHandler(handlerB)
            .addHandler(handlerBbis)
            .build()

        eventHandlers.getHandlers<EventA>().shouldContainExactly(handlerA)
        eventHandlers.getHandlers<EventB>().shouldContainExactly(handlerB, handlerBbis)
    }

    @Test
    internal fun `handle event by event type`() {
        val handlerA = mockk<EventHandler<EventA>>(relaxed = true)

        val eventHandlers = EventHandlers.builder()
            .addHandler(handlerA)
            .build()

        val event = EventA()
        runTest {
            eventHandlers.handleEvent(event, mockk())
        }

        coVerify(exactly = 1) { handlerA.handle(event, any()) }
    }
}