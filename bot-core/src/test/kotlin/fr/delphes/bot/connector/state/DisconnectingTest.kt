package fr.delphes.bot.connector.state

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.bot.connector.ConnectorRuntimeForTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class DisconnectingTest {
    @Test
    internal fun `when the connector is configured should become configured with new configuration`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toDisconnecting().handle(Configure(NEW_CONFIGURATION))
        }

        newState shouldBe Configured(NEW_CONFIGURATION)
    }

    @Test
    internal fun `when connection is requested should become connecting`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toDisconnecting().handle(ConnectionRequested())
        }

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    @Test
    internal fun `when connection is successful with the same configuration should stay disconnecting`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toDisconnecting().handle(ConnectionSuccessful(CURRENT_CONFIGURATION, ConnectorRuntimeForTest))
        }

        newState shouldBe Disconnecting(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    @Test
    internal fun `when connection is successful with other configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toDisconnecting().handle(ConnectionSuccessful(OTHER_CONFIGURATION, ConnectorRuntimeForTest))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }

    @Test
    internal fun `when disconnection is requested should stay disconnecting`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toDisconnecting().handle(DisconnectionRequested())
        }

        newState shouldBe Disconnecting(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    @Test
    internal fun `when disconnection is successful with the same configuration should become configured`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toDisconnecting().handle(DisconnectionSuccessful(CURRENT_CONFIGURATION))
        }

        newState shouldBe Configured(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when disconnection is successful with the another configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toDisconnecting().handle(DisconnectionSuccessful(OTHER_CONFIGURATION))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "disconnection received for another configuration")
    }

    @Test
    internal fun `when error occurred should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toDisconnecting().handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    @Test
    internal fun `when error occurred for another configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toDisconnecting().handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"))
        }
        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }

    private fun ConfigurationStub.toDisconnecting(): Disconnecting<ConfigurationStub, ConnectorRuntimeForTest> = Disconnecting(this, ConnectorRuntimeForTest)

    companion object {
        val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        val NEW_CONFIGURATION = ConfigurationStub("newValue")
        val OTHER_CONFIGURATION = ConfigurationStub("otherValue")
    }
}