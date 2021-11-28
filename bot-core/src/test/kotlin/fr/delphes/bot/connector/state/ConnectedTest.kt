package fr.delphes.bot.connector.state

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.bot.connector.ConnectorRuntimeForTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class ConnectedTest {
    @Test
    internal fun `when the connector is configured should have configured state`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(Configure(NEW_CONFIGURATION))
        }

        newState shouldBe Configured(NEW_CONFIGURATION)
    }

    @Test
    internal fun `when the connector is configured with the same configuration should stay with connected state`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(Configure(CURRENT_CONFIGURATION))
        }

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    @Test
    internal fun `when connection is requested should stay connected with configured configuration`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(ConnectionRequested())
        }

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    @Test
    internal fun `when connection is successful with same configuration should stay connected`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(ConnectionSuccessful(CURRENT_CONFIGURATION, ConnectorRuntimeForTest))
        }

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    @Test
    internal fun `when connection is successful with different configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(ConnectionSuccessful(OTHER_CONFIGURATION, ConnectorRuntimeForTest))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "connected with different configuration")
    }

    @Test
    internal fun `when disconnection is requested should become disconnecting with configured configuration`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(DisconnectionRequested())
        }

        newState shouldBe Disconnecting(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    @Test
    internal fun `when disconnection is successful with same configuration should become configured`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(DisconnectionSuccessful(CURRENT_CONFIGURATION))
        }

        newState shouldBe Configured(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when disconnection is successful with another configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(DisconnectionSuccessful(OTHER_CONFIGURATION))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "disconnection received for another configuration")
    }

    @Test
    internal fun `when error occurred should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"))
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    @Test
    internal fun `when error occurred for another configuration should be in error`() {
        val newState = runBlocking {
            CURRENT_CONFIGURATION.toConnected().handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"))
        }
        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }

    private fun ConfigurationStub.toConnected(): Connected<ConfigurationStub, ConnectorRuntimeForTest> = Connected(this, ConnectorRuntimeForTest)

    companion object {
        val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        val NEW_CONFIGURATION = ConfigurationStub("newValue")
        val OTHER_CONFIGURATION = ConfigurationStub("otherValue")
    }
}