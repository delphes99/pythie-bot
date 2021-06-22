package fr.delphes.utils.store

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class StoreTest {
    @Test
    internal fun init() {
        Store(INITIAL_STATE, mockk<ReducerWrapper<StateImpl, ActionImpl>>()).currentState shouldBe INITIAL_STATE
    }

    @Test
    internal fun `call reducer on action`() {
        val reducer = buildReducer()
        val store = Store(INITIAL_STATE, reducer.wrap())

        store.dispatch(ACTION)

        verify(exactly = 1) { reducer.apply(ACTION, INITIAL_STATE) }
    }

    @Test
    internal fun `call all reducers on action`() {
        val reducer1 = buildReducer()
        val reducer2 = buildReducer()
        val store = Store(INITIAL_STATE, reducer1.wrap(), reducer2.wrap())

        store.dispatch(ACTION)

        verify(exactly = 1) { reducer1.apply(ACTION, any()) }
        verify(exactly = 1) { reducer2.apply(ACTION, any()) }
    }

    @Test
    internal fun `called reducer apply the new state`() {
        val store = Store(INITIAL_STATE, Reducer<StateImpl, ActionImpl> { _, _ -> NEW_STATE }.wrap())

        store.dispatch(ACTION)

        store.currentState shouldBe NEW_STATE
    }

    private fun buildReducer() = mockk<Reducer<StateImpl, ActionImpl>>(relaxed = true)

    data class ActionImpl(val actionPayload: String) : Action

    private data class StateImpl(
        val value: String
    )


    companion object {
        private val ACTION = ActionImpl("action")
        private val INITIAL_STATE = StateImpl("init")
        private val NEW_STATE = StateImpl("new state")
    }
}

