package fr.delphes.bot.connector.connectionstate

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.bot.connector.ConnectorRuntimeForTest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ConnectedTest : ShouldSpec({
    should("when the connector is configured should have configured state") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(Configure(NEW_CONFIGURATION))

        newState shouldBe Disconnected(NEW_CONFIGURATION)
    }

    should("when the connector is configured with the same configuration should stay with connected state") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(Configure(CURRENT_CONFIGURATION))

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("when connection is requested should stay connected with configured configuration") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(ConnectionRequested())

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("when connection is successful with same configuration should stay connected") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(ConnectionSuccessful(CURRENT_CONFIGURATION, ConnectorRuntimeForTest))

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("when connection is successful with different configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(ConnectionSuccessful(OTHER_CONFIGURATION, ConnectorRuntimeForTest))

        newState shouldBe InError(CURRENT_CONFIGURATION, "connected with different configuration")
    }

    should("when disconnection is requested should become disconnecting with configured configuration") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(DisconnectionRequested())

        newState shouldBe Disconnecting(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("when disconnection is successful with same configuration should become configured") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(DisconnectionSuccessful(CURRENT_CONFIGURATION))

        newState shouldBe Disconnected(CURRENT_CONFIGURATION)
    }

    should("when disconnection is successful with another configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(DisconnectionSuccessful(OTHER_CONFIGURATION))

        newState shouldBe InError(CURRENT_CONFIGURATION, "disconnection received for another configuration")
    }

    should("when error occurred should be in error") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"))

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    should("when error occurred for another configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toConnected().handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"))

        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }
}) {
    companion object {
        private val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        private val NEW_CONFIGURATION = ConfigurationStub("newValue")
        private val OTHER_CONFIGURATION = ConfigurationStub("otherValue")

        private fun ConfigurationStub.toConnected(): Connected<ConfigurationStub, ConnectorRuntimeForTest> = Connected(this, ConnectorRuntimeForTest)
    }
}