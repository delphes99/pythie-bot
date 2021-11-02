package fr.delphes.bot.connector

import fr.delphes.bot.connector.state.Configured
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.Connecting
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.connector.state.ErrorOccurred
import fr.delphes.bot.connector.state.InError
import fr.delphes.bot.connector.state.NotConfigured
import fr.delphes.utils.Repository
import fr.delphes.utils.RepositoryWithInit
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ConnectorStateMachineTest {
    private val repository = mockk<RepositoryWithInit<ConfigurationStub>>()
    private val stateMachine = ConnectorStateMachine(repository)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    internal fun `state with configuration loaded is configured`() {
        repository `will load` CONFIGURATION

        runBlocking {
            stateMachine.load()
        }

        stateMachine.state shouldBe Configured(CONFIGURATION)
    }

    @Test
    internal fun `state with no configuration is not configured`() {
        repository `will load` null

        runBlocking {
            stateMachine.load()
        }

        stateMachine.state shouldBe NotConfigured()
    }

    @Test
    internal fun `state after connection request is connecting`() {
        repository `will load` CONFIGURATION

        runBlocking {
            stateMachine.load()

            stateMachine.connect {
                delay(1)
                ConnectionSuccessful(CONFIGURATION)
            }
        }

        stateMachine.state shouldBe Connecting(CONFIGURATION)
    }

    @Test
    internal fun `state after connection successful is connected`() {
        repository `will load` CONFIGURATION

        runBlocking {
            stateMachine.load()

            stateMachine.connect {
                delay(1)
                ConnectionSuccessful(CONFIGURATION)
            }

            delay(50)
            stateMachine.state shouldBe Connected(CONFIGURATION)
        }
    }

    @Test
    internal fun `state after error has occured is in error`() {
        repository `will load` CONFIGURATION

        runBlocking {
            stateMachine.load()

            stateMachine.connect {
                delay(1)
                ErrorOccurred(CONFIGURATION, "some error")
            }

            delay(50)
            stateMachine.state shouldBe InError(CONFIGURATION, "some error")
        }
    }

    @Test
    internal fun `state after an exception is in error`() {
        repository `will load` CONFIGURATION

        runBlocking {
            stateMachine.load()

            stateMachine.connect {
                delay(1)
                withContext(Dispatchers.Default + SupervisorJob()) {
                    throw Exception("some error")
                }
            }

            delay(50)
            stateMachine.state shouldBe InError(CONFIGURATION, "Error has occurred : some error")
        }
    }

    private infix fun Repository<ConfigurationStub>.`will load`(result: ConfigurationStub?) {
        coEvery { load() } returns result
    }

    companion object {
        private val CONFIGURATION = ConfigurationStub("value")
    }
}