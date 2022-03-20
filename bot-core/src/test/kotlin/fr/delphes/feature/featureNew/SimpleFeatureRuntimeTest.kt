package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class SimpleFeatureRuntimeTest {
    @Test
    internal fun `execute responses`() {
        val runtime = SimpleFeatureRuntime.noState(
            IncomingEventFilters(listOf(IncomingEventFilter { _,_ -> true }))
        ) { listOf(OUTGOING_EVENT) }

        runtime.execute(INCOMING_EVENT).shouldContainExactly(OUTGOING_EVENT)
    }

    @Test
    internal fun `should not execute responses if filters are off`() {
        val runtime = SimpleFeatureRuntime.noState(
            IncomingEventFilters(listOf(IncomingEventFilter { _,_ -> false }))
        ) { listOf(OUTGOING_EVENT) }

        runtime.execute(INCOMING_EVENT).shouldBeEmpty()
    }

    //TODO state

    companion object {
        val INCOMING_EVENT = mockk<IncomingEvent>()
        val OUTGOING_EVENT = mockk<OutgoingEvent>()
    }
}