package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class IncomingEventFilterTest {
    @Test
    internal fun `does not activate if filter is off`() {
        val incomingEvent = mockk<IncomingEvent>()
        val isApplicable = IncomingEventFilters(
            listOf(FILTER_OFF)
        ).isApplicable(incomingEvent, NoState)

        isApplicable shouldBe false
    }

    @Test
    internal fun `activate if filter is on`() {
        val incomingEvent = mockk<IncomingEvent>()
        val isApplicable = IncomingEventFilters(
            listOf(FILTER_ON)
        ).isApplicable(incomingEvent, NoState)

        isApplicable shouldBe true
    }

    @Test
    internal fun `activate if all filter are on`() {
        val incomingEvent = mockk<IncomingEvent>()
        val isApplicable = IncomingEventFilters(
            listOf(FILTER_ON, FILTER_ON)
        ).isApplicable(incomingEvent, NoState)

        isApplicable shouldBe true
    }

    @Test
    internal fun `does not activate if one filter is off`() {
        val incomingEvent = mockk<IncomingEvent>()
        val isApplicable = IncomingEventFilters(
            listOf(FILTER_ON, FILTER_OFF, FILTER_ON)
        ).isApplicable(incomingEvent, NoState)

        isApplicable shouldBe false
    }

    companion object {
        val FILTER_OFF = IncomingEventFilter<NoState> { _, _ -> false }
        val FILTER_ON = IncomingEventFilter<NoState> { _, _ -> true }
    }
}

