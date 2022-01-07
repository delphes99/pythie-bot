package fr.delphes.bot.connector

import fr.delphes.utils.Repository
import kotlinx.coroutines.runBlocking

class SimpleConfigurationManager<CONFIGURATION: ConnectorConfiguration>(
    private val repository: Repository<CONFIGURATION>
) : ConfigurationManager<CONFIGURATION> {
    override var configuration: CONFIGURATION? = runBlocking { repository.load() }

    override suspend fun configure(configuration: CONFIGURATION) {
        repository.save(configuration)
    }
}