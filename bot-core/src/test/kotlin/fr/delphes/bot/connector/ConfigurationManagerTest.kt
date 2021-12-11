package fr.delphes.bot.connector

import fr.delphes.utils.Repository
import fr.delphes.utils.RepositoryWithInit
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class ConfigurationManagerTest {
    private val repository = mockk<RepositoryWithInit<ConfigurationStub>>()

    @Test
    internal fun `load configuration when construct`() {
        repository `will load` CONFIGURATION

        val stateMachine = ConfigurationManager(repository)

        stateMachine.configuration shouldBe CONFIGURATION
    }

    @Test
    internal fun `load configuration when construct (no configuration stored)`() {
        repository `will load` null

        val stateMachine = ConfigurationManager(repository)

        stateMachine.configuration.shouldBeNull()
    }

    private infix fun Repository<ConfigurationStub>.`will load`(result: ConfigurationStub?) {
        coEvery { load() } returns result
    }

    companion object {
        private val CONFIGURATION = ConfigurationStub("value")
    }
}