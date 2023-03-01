package fr.delphes.bot.connector.connectionstate

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.bot.connector.ConnectorRuntimeForTest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class DisconnectingTest : ShouldSpec({
    should("when the connector is configured should become configured with new configuration") {
        val newState = CURRENT_CONFIGURATION.toDisconnecting().handle(Configure(NEW_CONFIGURATION))

        newState shouldBe Disconnected(NEW_CONFIGURATION)
    }

    should("when connection is requested should become connecting") {
        val newState = CURRENT_CONFIGURATION.toDisconnecting().handle(ConnectionRequested())

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("when connection is successful with the same configuration should stay disconnecting") {
        val newState = CURRENT_CONFIGURATION.toDisconnecting().handle(ConnectionSuccessful(CURRENT_CONFIGURATION, ConnectorRuntimeForTest))

        newState shouldBe Disconnecting(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("when connection is successful with other configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toDisconnecting().handle(ConnectionSuccessful(OTHER_CONFIGURATION, ConnectorRuntimeForTest))

        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }

    should("when disconnection is requested should stay disconnecting") {
        val newState = CURRENT_CONFIGURATION.toDisconnecting().handle(DisconnectionRequested())

        newState shouldBe Disconnecting(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("when disconnection is successful with the same configuration should become configured") {
        val newState = CURRENT_CONFIGURATION.toDisconnecting().handle(DisconnectionSuccessful(CURRENT_CONFIGURATION))

        newState shouldBe Disconnected(CURRENT_CONFIGURATION)
    }

    should("when disconnection is successful with the another configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toDisconnecting().handle(DisconnectionSuccessful(OTHER_CONFIGURATION))

        newState shouldBe InError(CURRENT_CONFIGURATION, "disconnection received for another configuration")
    }

    should("when error occurred should be in error") {
        val newState = CURRENT_CONFIGURATION.toDisconnecting().handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"))

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    should("when error occurred for another configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toDisconnecting().handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"))

        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }
}) {
    companion object {
        private val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        private val NEW_CONFIGURATION = ConfigurationStub("newValue")
        private val OTHER_CONFIGURATION = ConfigurationStub("otherValue")

        private fun ConfigurationStub.toDisconnecting(): Disconnecting<ConfigurationStub, ConnectorRuntimeForTest> = Disconnecting(this, ConnectorRuntimeForTest)
    }
}