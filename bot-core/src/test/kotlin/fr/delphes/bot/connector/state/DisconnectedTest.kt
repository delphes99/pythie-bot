package fr.delphes.bot.connector.state

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.bot.connector.ConnectorRuntimeForTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class DisconnectedTest {
    @Test
    internal fun `when the connector is configured should have configured state`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConfigured().handle(Configure(NEW_CONFIGURATION))
        }

        newState shouldBe Disconnected(NEW_CONFIGURATION)
    }

    @Test
    internal fun `when connection is requested should have connecting state with configured configuration`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConfigured().handle(ConnectionRequested())
        }

        newState shouldBe Connecting(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when connection is successful with same configuration should be connected`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConfigured().handle(ConnectionSuccessful(CURRENT_CONFIGURATION, ConnectorRuntimeForTest))
        }

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    @Test
    internal fun `when connection is successful with different configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConfigured().handle(ConnectionSuccessful(OTHER_CONFIGURATION, ConnectorRuntimeForTest))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "connected with different configuration")
    }

    @Test
    internal fun `when disconnection is requested should remain configured state with configured configuration`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConfigured().handle(DisconnectionRequested())
        }

        newState shouldBe Disconnected(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when disconnection is successful with the same configuration should remain configured state`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConfigured().handle(DisconnectionSuccessful(CURRENT_CONFIGURATION))
        }

        newState shouldBe Disconnected(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when disconnection is successful with the other configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConfigured().handle(DisconnectionSuccessful(OTHER_CONFIGURATION))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "disconnection received for another configuration")
    }

    @Test
    internal fun `when error occurred should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConfigured().handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    @Test
    internal fun `when error occurred for another configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConfigured().handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"))
        }
        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }

    private fun ConfigurationStub.toConfigured(): Disconnected<ConfigurationStub, ConnectorRuntimeForTest> = Disconnected(this)

    companion object {
        val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        val NEW_CONFIGURATION = ConfigurationStub("newValue")
        val OTHER_CONFIGURATION = ConfigurationStub("otherValue")
    }
}