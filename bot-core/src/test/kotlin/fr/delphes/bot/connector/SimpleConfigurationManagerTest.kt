package fr.delphes.bot.connector

import fr.delphes.utils.Repository
import fr.delphes.utils.RepositoryWithInit
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test

class SimpleConfigurationManagerTest : ShouldSpec({
    val repository = mockk<RepositoryWithInit<ConfigurationStub>>()

    should("load configuration when construct") {
        repository `will load` CONFIGURATION

        val stateMachine = SimpleConfigurationManager(repository)

        stateMachine.configuration shouldBe CONFIGURATION
    }

    should("load configuration when construct (no configuration stored)") {
        repository `will load` null

        val stateMachine = SimpleConfigurationManager(repository)

        stateMachine.configuration.shouldBeNull()
    }
}) {
    companion object {
        private val CONFIGURATION = ConfigurationStub("value")

        private infix fun Repository<ConfigurationStub>.`will load`(result: ConfigurationStub?) {
            coEvery { load() } returns result
        }
    }
}