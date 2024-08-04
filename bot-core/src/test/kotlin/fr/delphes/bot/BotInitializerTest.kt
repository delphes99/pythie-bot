package fr.delphes.bot

import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorState
import fr.delphes.state.StateId
import fr.delphes.state.StateManager
import fr.delphes.state.enumeration.EnumerationState
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class BotInitializerTest : ShouldSpec({
    fun connectorWith(
        connectorStates: List<ConnectorState> = emptyList(),
        enumerationStates: List<EnumerationState<*>> = emptyList(),
    ) =
        mockk<Connector<*, *>> {
            every { states } returns connectorStates
            every { this@mockk.enumerationStates } returns enumerationStates
        }

    context("initialize states from connector") {
        should("shoud add connector state") {
            val stateManager = StateManager()
            val addedState = connectorState
            val connector = connectorWith(connectorStates = listOf(addedState))

            val botInitializer = BotInitializer(stateManager, listOf(connector))

            botInitializer.initialize()

            stateManager.getStateOrNull(connectorStateId) shouldBe addedState
        }
        should("shoud add enumeration state") {
            val stateManager = StateManager()
            val addedState = enumerationState
            val connector = connectorWith(enumerationStates = listOf(addedState))

            val botInitializer = BotInitializer(stateManager, listOf(connector))

            botInitializer.initialize()

            stateManager.getStateOrNull(enumerationStateId) shouldBe addedState
        }
    }

}) {
    companion object {
        private class TestConnectorState(
            override val id: StateId<TestConnectorState>,
        ) : ConnectorState

        private val connectorStateId = StateId.from<TestConnectorState>("connector state test id")
        private val connectorState = TestConnectorState(connectorStateId)

        private class TestEnumerationState(
            override val id: StateId<TestEnumerationState>,
        ) : EnumerationState<String> {
            override suspend fun getItems() = error("Not implemented")

            override fun deserialize(serializeValue: String) = error("Not implemented")
        }

        private val enumerationStateId = StateId.from<TestEnumerationState>("enumeration state test id")
        private val enumerationState = TestEnumerationState(enumerationStateId)
    }
}
