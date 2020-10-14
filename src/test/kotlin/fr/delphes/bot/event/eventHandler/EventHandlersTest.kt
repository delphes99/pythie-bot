package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.event.incoming.IncomingEvent
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(eventHandlers.getHandlers<EventA>()).containsExactlyInAnyOrder(handlerA)
        assertThat(eventHandlers.getHandlers<EventB>()).containsExactlyInAnyOrder(handlerB, handlerBbis)
    }

    @Test
    internal fun `handle event by event type`() {
        val eventHandlers = EventHandlers()
        val handlerA = mockk<EventHandler<EventA>>(relaxed = true)

        eventHandlers.addHandler(handlerA)

        val event = EventA()
        eventHandlers.handleEvent(event)

        verify(exactly = 1) { handlerA.handle(event) }
    }
}