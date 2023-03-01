package fr.delphes.bot.connector

import fr.delphes.bot.connector.connectionstate.Connected
import fr.delphes.bot.connector.connectionstate.Connecting
import fr.delphes.bot.connector.connectionstate.ConnectionRequested
import fr.delphes.bot.connector.connectionstate.ConnectionSuccessful
import fr.delphes.bot.connector.connectionstate.ConnectorState
import fr.delphes.bot.connector.connectionstate.ConnectorTransition
import fr.delphes.bot.connector.connectionstate.Disconnected
import fr.delphes.bot.connector.connectionstate.ErrorOccurred
import fr.delphes.bot.connector.connectionstate.InError
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class StandAloneConnectorStateMachineTest : ShouldSpec({
    should("state after connection request is connecting") {
        val stateMachine = TestConnectionManager()

        stateMachine.dispatchTransition(ConnectionRequested())

        stateMachine.state shouldBe Connecting(CONFIGURATION)
    }

    should("state after connection successful is connected") {
        val stateMachine = TestConnectionManager()

        stateMachine.dispatchTransition(ConnectionRequested())

        delay(50)
        stateMachine.state shouldBe Connected(CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("state after error has occurred is in error") {
        val stateMachine = TestConnectionManager(
            ErrorOccurred(CONFIGURATION, "some error")
        )

        stateMachine.dispatchTransition(ConnectionRequested())

        delay(50)
        stateMachine.state shouldBe InError(CONFIGURATION, "some error")
    }

    should("state after an exception is in error") {
        val stateMachine = TestConnectionManager({
            withContext(Dispatchers.Default + SupervisorJob()) {
                throw Exception("some error")
            }
        })

        stateMachine.dispatchTransition(ConnectionRequested())

        delay(50)
        stateMachine.state shouldBe InError(CONFIGURATION, "Error has occurred : some error")
    }

    should("change state when connected will kill the runtime") {
        val runtime = mockk<ConnectorRuntimeForTest>(relaxed = true)

        val stateMachine = TestConnectionManager(
            initialState = Connected(CONFIGURATION, runtime),
        )

        stateMachine.dispatchTransition(ErrorOccurred(CONFIGURATION, "error"))

        coVerify(exactly = 1) { runtime.kill() }
    }
}) {
    companion object {
        private val CONFIGURATION = ConfigurationStub("value")
    }

    class TestConnectionManager(
        private val transitionToReturnAtConnection: suspend () -> ConnectorTransition<ConfigurationStub, ConnectorRuntimeForTest>,
        initialState: ConnectorState<ConfigurationStub, ConnectorRuntimeForTest> = Disconnected(CONFIGURATION),
    ) :
        StandAloneConnectionManager<ConfigurationStub, ConnectorRuntimeForTest>(
            InMemoryConfigurationManager(CONFIGURATION),
            initialState
        ) {

        constructor(
            transitionToReturnAtConnection: ConnectorTransition<ConfigurationStub, ConnectorRuntimeForTest> = ConnectionSuccessful(CONFIGURATION, ConnectorRuntimeForTest),
            initialState: ConnectorState<ConfigurationStub, ConnectorRuntimeForTest> = Disconnected(CONFIGURATION),
        ) : this(
            { transitionToReturnAtConnection },
            initialState
        )

        override val connectionName = "connection for test"

        override suspend fun doConnection(configuration: ConfigurationStub): ConnectorTransition<ConfigurationStub, ConnectorRuntimeForTest> {
            return transitionToReturnAtConnection()
        }

        override suspend fun execute(event: OutgoingEvent) {
            //Nothing
        }
    }
}