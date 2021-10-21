package fr.delphes.bot.connector.state

import fr.delphes.utils.Repository
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class ConfiguredTest {
    private val repository = mockk<Repository<ConfigurationStub>>(relaxed = true)

    @Test
    internal fun `when the connector is configured should have configured state`() {
        val newState = runBlocking {
            Configured(CURRENT_CONFIGURATION).handle(Configure(NEW_CONFIGURATION), repository)
        }

        newState shouldBe Configured(NEW_CONFIGURATION)
    }

    @Test
    internal fun `when the connector is configured should save new configuration`() {
        runBlocking {
            Configured(CURRENT_CONFIGURATION).handle(Configure(NEW_CONFIGURATION), repository)
        }

        coVerify(exactly = 1) { repository.save(NEW_CONFIGURATION) }
    }

    @Test
    internal fun `when connection is requested should have connecting state with configured configuration`() {
        val newState = runBlocking {
            Configured(CURRENT_CONFIGURATION).handle(ConnectionRequested(), repository)
        }

        newState shouldBe Connecting(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when connection is successful with same configuration should be connected`() {
        val newState = runBlocking {
            Configured(CURRENT_CONFIGURATION).handle(ConnectionSuccessful(CURRENT_CONFIGURATION), repository)
        }

        newState shouldBe Connected(CURRENT_CONFIGURATION)
    }

    @Test
    internal fun `when connection is successful with different configuration should be in error`() {
        val newState = runBlocking {
            Configured(CURRENT_CONFIGURATION).handle(ConnectionSuccessful(OTHER_CONFIGURATION), repository)
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "connected with different configuration")
    }

    @Test
    internal fun `when error occurred should be in error`() {
        val newState = runBlocking {
            Configured(CURRENT_CONFIGURATION).handle(ErrorOccurred(CURRENT_CONFIGURATION, "some error message"), repository)
        }

        newState shouldBe InError(CURRENT_CONFIGURATION, "some error message")
    }

    @Test
    internal fun `when error occurred for another configuration should be in error`() {
        val newState = runBlocking {
            Configured(CURRENT_CONFIGURATION).handle(ErrorOccurred(OTHER_CONFIGURATION, "some error message"), repository)
        }
        newState shouldBe InError(CURRENT_CONFIGURATION, "error for another configuration")
    }

    companion object {
        val CURRENT_CONFIGURATION = ConfigurationStub("currentValue")
        val NEW_CONFIGURATION = ConfigurationStub("newValue")
        val OTHER_CONFIGURATION = ConfigurationStub("otherValue")
    }
}