package fr.delphes.features

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.state.StateManager
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class TestFeatureReturnExecutionContext(
    @PublishedApi
    internal val stateManager: StateManager,
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

    inline fun <reified T : State> shouldState(stateId: StateId<T>, executeAssertion: T.() -> Unit) {
        val state = stateManager.getState(stateId)
        state.executeAssertion()
    }

    val bot = mockk<Bot> { every { featuresManager.stateManager } returns stateManager }
}
