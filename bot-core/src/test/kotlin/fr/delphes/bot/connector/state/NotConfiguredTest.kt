package fr.delphes.bot.connector.state

import fr.delphes.utils.Repository
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class NotConfiguredTest {
    private val repository = mockk<Repository<ConfigurationStub>>(relaxed = true)

    @Test
    internal fun `should have no configuration`() {
        NotConfigured<ConfigurationStub>().configuration.shouldBeNull()
    }

    @Test
    internal fun `when the connector is configured should have configured state`() {
        val newConfiguration = ConfigurationStub("newValue")

        val newState = runBlocking {
            NotConfigured<ConfigurationStub>().handle(Configure(newConfiguration), repository)
        }

        newState shouldBe Configured(newConfiguration)
    }

    @Test
    internal fun `when the connector is configured should save new configuration`() {
        val newConfiguration = ConfigurationStub("newValue")

        runBlocking {
            NotConfigured<ConfigurationStub>().handle(Configure(newConfiguration), repository)
        }

        coVerify(exactly = 1) { repository.save(newConfiguration) }
    }

    @ParameterizedTest(name = "when {1} is received should return current state")
    @MethodSource("unsupportedTransitions")
    internal fun `when unsupported transitions are received should return current state`(
        transition: ConnectorTransition<ConfigurationStub>,
        name: String
    ) {
        val oldState = NotConfigured<ConfigurationStub>()
        val newState = runBlocking {
            oldState.handle(transition, repository)
        }

        newState shouldBe oldState
    }

    companion object {
        private val someConfiguration = ConfigurationStub("newValue")

        @JvmStatic
        fun unsupportedTransitions() = listOf(
            Arguments.of(ConnectionRequested<ConfigurationStub>(), "Connect"),
            Arguments.of(ConnectionSuccessful(someConfiguration), "Connected"),
            Arguments.of(ErrorOccurred(someConfiguration, "error"), "Error"),
        )
    }
}