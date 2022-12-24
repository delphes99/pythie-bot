package fr.delphes.state

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class StateManagerTest : ShouldSpec({
    should("should return null if state does not exist") {
        val stateManager = StateManager()

        stateManager.getState(stateId).shouldBeNull()
    }
    should("should put value according to type and id bis") {
        val stateManager = StateManager()

        val addedState = TestState(stateId)
        stateManager.put(addedState)

        stateManager.getState(stateId) shouldBe addedState
    }
    should("should put value according to type and id") {
        val stateManager = StateManager()

        val addedState = TestState(stateId)
        stateManager.put(addedState)
        val anotherAddedState = TestState(anotherStateId)
        stateManager.put(anotherAddedState)

        stateManager.getState(stateId) shouldBe addedState
    }
    should("should put default value if no state found") {
        val stateManager = StateManager()

        val addedState = TestState(stateId)
        stateManager.getOrPut(stateId) { addedState }

        stateManager.getState(stateId) shouldBe addedState
    }
}) {
    companion object {
        private val stateId = StateId.from<TestState>("test id")
        private val anotherStateId = StateId.from<TestState>("other test id")

        private class TestState(
            override val id: StateId<TestState> = stateId
        ) : State
    }
}

