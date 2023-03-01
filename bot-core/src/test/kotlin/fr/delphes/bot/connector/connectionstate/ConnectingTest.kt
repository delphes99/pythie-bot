package fr.delphes.bot.connector.connectionstate

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.bot.connector.ConnectorRuntimeForTest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ConnectingTest : ShouldSpec({
    should("when the connector is configured should have configured state") {
        val newState = CURRENT_CONFIGURATION.toConnecting().handle(Configure(NEW_CONFIGURATION))

        newState shouldBe Disconnected(NEW_CONFIGURATION)
    }

    should("when the connector is configured with the same configuration should stay with connecting state") {
        val newState = CURRENT_CONFIGURATION.toConnecting().handle(Configure(CURRENT_CONFIGURATION))

        newState shouldBe Connecting(CURRENT_CONFIGURATION)
    }

    should("when connection is requested should stay connecting with configured configuration") {
        val newState = CURRENT_CONFIGURATION.toConnecting().handle(ConnectionRequested())

        newState shouldBe Connecting(CURRENT_CONFIGURATION)
    }

    should("when disconnection is requested should become configured with configured configuration") {
        val newState = CURRENT_CONFIGURATION.toConnecting().handle(DisconnectionRequested())

        newState shouldBe Disconnected(CURRENT_CONFIGURATION)
    }

    should("when connection is successful with same configuration should be connected") {
        val newState = CURRENT_CONFIGURATION.toConnecting().handle(ConnectionSuccessful(CURRENT_CONFIGURATION, ConnectorRuntimeForTest))

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("when connection is successful with different configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toConnecting().handle(ConnectionSuccessful(OTHER_CONFIGURATION, ConnectorRuntimeForTest))

        newState shouldBe InError(CURRENT_CONFIGURATION, "connected with different configuration")
    }

    should("when error occurred should be in error") {
        val newState = CURRENT_CONFIGURATION.toConnecting().handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"))

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    should("when error occurred for another configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toConnecting().handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"))

        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }
}) {
    companion object {
        private val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        private val NEW_CONFIGURATION = ConfigurationStub("newValue")
        private val OTHER_CONFIGURATION = ConfigurationStub("otherValue")

        private fun ConfigurationStub.toConnecting(): Connecting<ConfigurationStub, ConnectorRuntimeForTest> = Connecting(this)
    }
}