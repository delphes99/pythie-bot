package fr.delphes.bot.connector.state

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.bot.connector.ConnectorRuntimeForTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class ConnectingTest {
    @Test
    internal fun `when the connector is configured should have configured state`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnecting().handle(Configure(NEW_CONFIGURATION))
        }

        newState shouldBe Disconnected(NEW_CONFIGURATION)
    }

    @Test
    internal fun `when the connector is configured with the same configuration should stay with connecting state`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnecting().handle(Configure(CURRENT_CONFIGURATION))
        }

        newState shouldBe Connecting(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when connection is requested should stay connecting with configured configuration`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnecting().handle(ConnectionRequested())
        }

        newState shouldBe Connecting(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when disconnection is requested should become configured with configured configuration`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnecting().handle(DisconnectionRequested())
        }

        newState shouldBe Disconnected(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when connection is successful with same configuration should be connected`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnecting().handle(ConnectionSuccessful(CURRENT_CONFIGURATION, ConnectorRuntimeForTest))
        }

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    @Test
    internal fun `when connection is successful with different configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnecting().handle(ConnectionSuccessful(OTHER_CONFIGURATION, ConnectorRuntimeForTest))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "connected with different configuration")
    }

    @Test
    internal fun `when error occurred should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnecting().handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    @Test
    internal fun `when error occurred for another configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnecting().handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"))
        }
        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }

    private fun ConfigurationStub.toConnecting(): Connecting<ConfigurationStub, ConnectorRuntimeForTest> = Connecting(this)

    companion object {
        val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        val NEW_CONFIGURATION = ConfigurationStub("newValue")
        val OTHER_CONFIGURATION = ConfigurationStub("otherValue")
    }
}