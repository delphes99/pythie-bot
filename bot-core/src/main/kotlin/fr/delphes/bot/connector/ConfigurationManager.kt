package fr.delphes.bot.connector

import fr.delphes.utils.Repository
import kotlinx.coroutines.runBlocking

class ConfigurationManager<CONFIGURATION: ConnectorConfiguration>(
    private val repository: Repository<CONFIGURATION>
) {
    var configuration: CONFIGURATION? = runBlocking { repository.load() }

    suspend fun configure(configuration: CONFIGURATION) {
        repository.save(configuration)
    }
}