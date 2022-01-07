package fr.delphes.connector.twitch

import fr.delphes.bot.connector.ConfigurationManager
import fr.delphes.bot.connector.SimpleConfigurationManager
import kotlinx.coroutines.runBlocking

class TwitchConfigurationManager(
    repository: TwitchConfigurationRepository
) : ConfigurationManager<TwitchConfiguration> {
    private val configurationManager = SimpleConfigurationManager(repository)

    override suspend fun configure(configuration: TwitchConfiguration) {
        configurationManager.configure(configuration)
    }

    override var configuration: TwitchConfiguration?
        get() = configurationManager.configuration
        set(value) {
            value?.also {
                runBlocking { configurationManager.configure(value) }
            }
        }

    val currentConfiguration get() = configurationManager.configuration ?: TwitchConfiguration.empty

    suspend fun configureAppCredential(clientId: String, clientSecret: String) {
        configure(currentConfiguration.setAppCredential(clientId, clientSecret))
    }

    suspend fun removeChannel(channelName: String) {
        configure(currentConfiguration.removeChannel(channelName))
    }
}