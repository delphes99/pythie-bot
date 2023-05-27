package fr.delphes.features

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.state.StateManager
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class TestFeatureReturnExecutionContext(
    private val stateManager: StateManager,
) {
    fun shouldHaveExecuteOutgoingEvent(event: OutgoingEvent) {
        coVerify { bot.processOutgoingEvent(event) }
    }

    fun shouldHaveExecuteOutgoingEvent(event: OutgoingEvent, times: Int) {
        coVerify(exactly = times) { bot.processOutgoingEvent(event) }
    }

    fun shouldHaveNotExecuteOutgoingEvent(event: OutgoingEvent) {
        shouldHaveExecuteOutgoingEvent(event, 0)
    }

    internal val bot = mockk<Bot> { every { featuresManager.stateManager } returns stateManager }
}
