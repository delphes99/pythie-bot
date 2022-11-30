package fr.delphes.bot.connector

import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.Connecting
import fr.delphes.bot.connector.state.ConnectionRequested
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.connector.state.ConnectorState
import fr.delphes.bot.connector.state.ConnectorTransition
import fr.delphes.bot.connector.state.Disconnected
import fr.delphes.bot.connector.state.ErrorOccurred
import fr.delphes.bot.connector.state.InError
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class StandAloneConnectorStateMachineTest : ShouldSpec({
    should("state after connection request is connecting") {
        val stateMachine = buildStateMachine(
            doConnection = { _, _ ->
                delay(1)
                ConnectionSuccessful(CONFIGURATION, ConnectorRuntimeForTest)
            },
            initialState = Disconnected(CONFIGURATION),
        )

        runBlocking {
            stateMachine.handle(ConnectionRequested())
        }

        stateMachine.state shouldBe Connecting(CONFIGURATION)
    }

    should("state after connection successful is connected") {
        val stateMachine = buildStateMachine(
            doConnection = { _, _ ->
                delay(1)
                ConnectionSuccessful(CONFIGURATION, ConnectorRuntimeForTest)
            },
            initialState = Disconnected(CONFIGURATION),
        )

        runBlocking {
            stateMachine.handle(ConnectionRequested())

            delay(50)
            stateMachine.state shouldBe Connected(CONFIGURATION, ConnectorRuntimeForTest)
        }
    }

    should("state after error has occurred is in error") {
        val stateMachine = buildStateMachine(
            doConnection = { _, _ ->
                delay(1)
                ErrorOccurred(CONFIGURATION, "some error")
            },
            initialState = Disconnected(CONFIGURATION),
        )

        runBlocking {
            stateMachine.handle(ConnectionRequested())

            delay(50)
            stateMachine.state shouldBe InError(CONFIGURATION, "some error")
        }
    }

    should("state after an exception is in error") {
        val stateMachine = buildStateMachine(
            doConnection = { _, _ ->
                delay(1)
                withContext(Dispatchers.Default + SupervisorJob()) {
                    throw Exception("some error")
                }
            },
            initialState = Disconnected(CONFIGURATION),
        )

        runBlocking {
            stateMachine.handle(ConnectionRequested())

            delay(50)
            stateMachine.state shouldBe InError(CONFIGURATION, "Error has occurred : some error")
        }
    }

    should("change state when connected will kill the runtime") {
        val runtime = mockk<ConnectorRuntimeForTest>(relaxed = true)

        val stateMachine = buildStateMachine(
            initialState = Connected(CONFIGURATION, runtime),
        )

        runBlocking {
            stateMachine.handle(ErrorOccurred(CONFIGURATION, "error"))

            coVerify(exactly = 1) { runtime.kill() }
        }
    }
}) {
    companion object {
        private val CONFIGURATION = ConfigurationStub("value")

        private fun buildStateMachine(
            initialState: ConnectorState<ConfigurationStub, ConnectorRuntimeForTest> = Disconnected(),
            doConnection: suspend CoroutineScope.(ConfigurationStub, suspend (ConnectorTransition<ConfigurationStub, ConnectorRuntimeForTest>) -> Unit) -> ConnectorTransition<ConfigurationStub, ConnectorRuntimeForTest> = { _, _ ->
                ConnectionSuccessful(CONFIGURATION, ConnectorRuntimeForTest)
            }
        ) =
            StandAloneConnectorStateMachine(
                connectionName = "Test",
                doConnection = doConnection,
                state = initialState,
                executeEvent = {}
            )
    }
}