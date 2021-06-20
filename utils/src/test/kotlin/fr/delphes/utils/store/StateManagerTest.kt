package fr.delphes.utils.store

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class StateManagerTest {
    @Test
    internal fun init() {
        StateManager(INITIAL_STATE, mockk<Reducer<StateImpl, ActionImpl>>()).currentState shouldBe INITIAL_STATE
    }

    @Test
    internal fun `call reducer on action`() {
        val reducer = buildReducer()
        val state = StateManager(INITIAL_STATE, reducer)

        state.handle(ACTION)

        verify(exactly = 1) { reducer.applyOn(INITIAL_STATE, ACTION) }
    }

    @Test
    internal fun `call all reducers on action`() {
        val reducer1 = buildReducer()
        val reducer2 = buildReducer()
        val state = StateManager(INITIAL_STATE, reducer1, reducer2)

        state.handle(ACTION)

        verify(exactly = 1) { reducer1.applyOn(any(), ACTION) }
        verify(exactly = 1) { reducer2.applyOn(any(), ACTION) }
    }

    @Test
    internal fun `called reducer apply the new state`() {
        val reducer = Reducer<StateImpl, ActionImpl>({ _, _ ->  NEW_STATE }, ActionImpl::class.java)
        val state = StateManager(INITIAL_STATE, reducer)

        state.handle(ACTION)

        state.currentState shouldBe NEW_STATE
    }

    private fun buildReducer() = mockk<Reducer<StateImpl, ActionImpl>>(relaxed = true)

    data class ActionImpl(val actionPayload: String) : Action

    companion object {
        private val ACTION = ActionImpl("action")
        private val INITIAL_STATE = StateImpl("init")
        private val NEW_STATE = StateImpl("new state")
    }
}


private data class StateImpl(
    val value: String
)
