package fr.delphes.bot.connector.state

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.bot.connector.ConnectorRuntimeForTest
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class NotConfiguredTest {
    @Test
    internal fun `should have no configuration`() {
        buildNotConfigured().configuration.shouldBeNull()
    }

    @Test
    internal fun `when the connector is configured should have configured state`() {
        val newConfiguration = ConfigurationStub("newValue")

        val newState = runBlocking {
            buildNotConfigured().handle(Configure(newConfiguration))
        }

        newState shouldBe Configured(newConfiguration)
    }

    @ParameterizedTest(name = "when {1} is received should return current state")
    @MethodSource("unsupportedTransitions")
    internal fun `when unsupported transitions are received should return current state`(
        transition: ConnectorTransition<ConfigurationStub, ConnectorRuntimeForTest>,
        name: String
    ) {
        val oldState = buildNotConfigured()
        val newState = runBlocking {
            oldState.handle(transition)
        }

        newState shouldBe oldState
    }

    private fun buildNotConfigured() = NotConfigured<ConfigurationStub, ConnectorRuntimeForTest>()

    @Test
    internal fun equals() {
        NotConfigured<String, ConnectorRuntimeForTest>() shouldBe NotConfigured()
        NotConfigured<String, ConnectorRuntimeForTest>() shouldNotBe "String"
    }

    companion object {
        private val someConfiguration = ConfigurationStub("newValue")

        @JvmStatic
        fun unsupportedTransitions() = listOf(
            Arguments.of(ConnectionRequested<ConfigurationStub, ConnectorRuntimeForTest>(), "Connect"),
            Arguments.of(DisconnectionRequested<ConfigurationStub, ConnectorRuntimeForTest>(), "Disconnect"),
            Arguments.of(DisconnectionSuccessful<ConfigurationStub, ConnectorRuntimeForTest>(someConfiguration), "Disconnected"),
            Arguments.of(ConnectionSuccessful(someConfiguration, ConnectorRuntimeForTest), "Connected"),
            Arguments.of(ErrorOccurred<ConfigurationStub, ConnectorRuntimeForTest>(someConfiguration, "error"), "Error"),
        )
    }
}