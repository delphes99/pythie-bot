package fr.delphes.utils.store

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class ReducerTest {
    @Test
    internal fun `call reducer if same action`() {
        val mutation = buildStateMutation()
        val reducer = Reducer(mutation, ActionImpl::class.java)

        reducer.applyOn(mockk(), ACTION)

        verify(exactly = 1) { mutation.invoke(any()) }
    }

    @Test
    internal fun `don't call reducer if not same action`() {
        val mutation = buildStateMutation()
        val reducer = Reducer(mutation, ActionImpl::class.java)

        reducer.applyOn(mockk(), OTHER_ACTION)

        verify(exactly = 0) { mutation.invoke(any()) }
    }

    private fun buildStateMutation(): (State, ActionImpl) -> State =
        mockk(relaxed = true)

    companion object {
        private val ACTION = ActionImpl()
        private val OTHER_ACTION = AnotherActionImpl()
    }

    private class State
    private class ActionImpl: Action
    private class AnotherActionImpl: Action
}
