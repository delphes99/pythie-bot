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
        val handlerA = mockk<LegacyEventHandler<EventA>>()
        val handlerB = mockk<LegacyEventHandler<EventB>>()
        val handlerBbis = mockk<LegacyEventHandler<EventB>>()
        val eventHandlers = LegacyEventHandlers.builder()
            .addHandler(handlerA)
            .addHandler(handlerB)
            .addHandler(handlerBbis)
            .build()

        eventHandlers.getHandlers<EventA>().shouldContainExactly(handlerA)
        eventHandlers.getHandlers<EventB>().shouldContainExactly(handlerB, handlerBbis)
    }

    @Test
    internal fun `handle event by event type`() {
        val handlerA = mockk<LegacyEventHandler<EventA>>(relaxed = true)

        val eventHandlers = LegacyEventHandlers.builder()
            .addHandler(handlerA)
            .build()

        val event = EventA()
        runTest {
            eventHandlers.handleEvent(event, mockk())
        }

        coVerify(exactly = 1) { handlerA.handle(event, any()) }
    }
}