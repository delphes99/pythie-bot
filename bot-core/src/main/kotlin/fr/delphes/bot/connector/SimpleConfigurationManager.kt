package fr.delphes.bot.connector

import fr.delphes.utils.Repository
import kotlinx.coroutines.runBlocking

class SimpleConfigurationManager<CONFIGURATION: ConnectorConfiguration>(
    private val repository: Repository<CONFIGURATION>
) : ConfigurationManager<CONFIGURATION> {
    override val configuration: CONFIGURATION? get() = runBlocking { repository.load() }

    override suspend fun configure(newConfiguration: CONFIGURATION) {
        repository.save(newConfiguration)
    }
}