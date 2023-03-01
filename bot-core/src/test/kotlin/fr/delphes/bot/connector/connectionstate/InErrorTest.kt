package fr.delphes.bot.connector.connectionstate

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.bot.connector.ConnectorRuntimeForTest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class InErrorTest : ShouldSpec({
    should("when the connector is configured should have configured state") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(Configure(NEW_CONFIGURATION))

        newState shouldBe Disconnected(NEW_CONFIGURATION)
    }

    should("when the connector is configured with the same configuration should stay in error") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(Configure(CURRENT_CONFIGURATION))

        newState shouldBe InError(CURRENT_CONFIGURATION, "old error")
    }

    should("when connection is requested should be connecting with configured configuration") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(ConnectionRequested())

        newState shouldBe Connecting(CURRENT_CONFIGURATION)
    }

    should("when connection is successful with same configuration should become connected") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(ConnectionSuccessful(CURRENT_CONFIGURATION, ConnectorRuntimeForTest))

        newState shouldBe Connected(CURRENT_CONFIGURATION, ConnectorRuntimeForTest)
    }

    should("when connection is successful with different configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(ConnectionSuccessful(OTHER_CONFIGURATION, ConnectorRuntimeForTest))

        newState shouldBe InError(CURRENT_CONFIGURATION, "connected with different configuration")
    }

    should("when disconnection is requested should stay in error with configured configuration") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(DisconnectionRequested())

        newState shouldBe InError(CURRENT_CONFIGURATION, "old error")
    }

    should("when disconnection is successful with the same configuration should become configured") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(DisconnectionSuccessful(CURRENT_CONFIGURATION))

        newState shouldBe Disconnected(CURRENT_CONFIGURATION)
    }

    should("when disconnection is successful with the other configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(DisconnectionSuccessful(OTHER_CONFIGURATION))

        newState shouldBe InError(CURRENT_CONFIGURATION, "disconnection received for another configuration")
    }

    should("when error occurred should be in error") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"))

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    should("when error occurred for another configuration should be in error") {
        val newState = CURRENT_CONFIGURATION.toInErrorWith("old error").handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"))

        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }
}) {
    companion object {
        private val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        private val NEW_CONFIGURATION = ConfigurationStub("newValue")
        private val OTHER_CONFIGURATION = ConfigurationStub("otherValue")

        private fun ConfigurationStub.toInErrorWith(errorMessage: String): InError<ConfigurationStub, ConnectorRuntimeForTest> = InError(this, errorMessage)
    }
}