package fr.delphes.state

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class StateManagerTest : ShouldSpec({
    should("should return null if state does not exist") {
        val stateManager = StateManager()

        stateManager.get<TestState>(stateId).shouldBeNull()
    }
    should("should put value according to type and id bis") {
        val stateManager = StateManager()

        val addedState = TestState(stateId)
        stateManager.put(addedState)

        stateManager.get<TestState>(stateId) shouldBe addedState
    }
    should("should put value according to type and id") {
        val stateManager = StateManager()

        val addedState = TestState(stateId)
        stateManager.put(addedState)
        val anotherAddedState = TestState(anotherStateId)
        stateManager.put(anotherAddedState)

        stateManager.get<TestState>(stateId) shouldBe addedState
    }
    should("should put default value if no state found") {
        val stateManager = StateManager()

        val addedState = TestState(stateId)
        stateManager.getOrPut(stateId) { addedState }

        stateManager.get<TestState>(stateId) shouldBe addedState
    }
}) {
    companion object {
        private val stateId = StateId("test id")
        private val anotherStateId = StateId("other test id")

        private class TestState(
            override val id: StateId = stateId
        ) : State
    }
}

