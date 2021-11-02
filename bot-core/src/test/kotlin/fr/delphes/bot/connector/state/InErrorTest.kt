package fr.delphes.bot.connector.state

import fr.delphes.bot.connector.ConfigurationStub
import fr.delphes.utils.RepositoryWithInit
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class InErrorTest {
    private val repository = mockk<RepositoryWithInit<ConfigurationStub>>(relaxed = true)

    @Test
    internal fun `when the connector is configured should have configured state`() {
        val newState = runBlocking {
            InError(CURRENT_CONFIGURATION, "old error").handle(Configure(NEW_CONFIGURATION), repository)
        }

        newState shouldBe Configured(NEW_CONFIGURATION)
    }

    @Test
    internal fun `when the connector is configured with the same configuration should stay in error`() {
        val newState = runBlocking {
            InError(CURRENT_CONFIGURATION, "old error").handle(Configure(CURRENT_CONFIGURATION), repository)
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "old error")
    }

    @Test
    internal fun `when connection is requested be connecting with configured configuration`() {
        val newState = runBlocking {
            InError(CURRENT_CONFIGURATION, "old error").handle(ConnectionRequested(), repository)
        }

        newState shouldBe Connecting(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when connection is successful with same configuration should stay connected`() {
        val newState = runBlocking {
            InError(CURRENT_CONFIGURATION, "old error").handle(ConnectionSuccessful(CURRENT_CONFIGURATION), repository)
        }

        newState shouldBe Connected(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when connection is successful with different configuration should be in error`() {
        val newState = runBlocking {
            InError(CURRENT_CONFIGURATION, "old error").handle(ConnectionSuccessful(OTHER_CONFIGURATION), repository)
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "connected with different configuration")
    }

    @Test
    internal fun `when error occurred should be in error`() {
        val newState = runBlocking {
            InError(CURRENT_CONFIGURATION, "old error").handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"), repository)
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    @Test
    internal fun `when error occurred for another configuration should be in error`() {
        val newState = runBlocking {
            InError(CURRENT_CONFIGURATION, "old error").handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"), repository)
        }
        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }

    companion object {
        val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        val NEW_CONFIGURATION = ConfigurationStub("newValue")
        val OTHER_CONFIGURATION = ConfigurationStub("otherValue")
    }
}