package fr.delphes.connector.twitch

import fr.delphes.bot.connector.ConfigurationManager
import fr.delphes.bot.connector.SimpleConfigurationManager
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.CredentialsManager

class TwitchConfigurationManager(
    repository: TwitchConfigurationRepository
) : ConfigurationManager<TwitchConfiguration>, AuthTokenRepository {
    private val configurationManager = SimpleConfigurationManager(repository)

    override suspend fun configure(newConfiguration: TwitchConfiguration) {
        configurationManager.configure(newConfiguration)
    }

    override val configuration: TwitchConfiguration?
        get() = configurationManager.configuration

    val currentConfiguration get() = configurationManager.configuration ?: TwitchConfiguration.empty

    suspend fun configureAppCredential(clientId: String, clientSecret: String) {
        configure(currentConfiguration.setAppCredential(clientId, clientSecret))
    }

    suspend fun removeChannel(channelName: String) {
        configure(currentConfiguration.removeChannel(channelName))
    }

    override fun getAppToken(): AuthToken? {
        return currentConfiguration.appToken
    }

    override suspend fun newAppToken(token: AuthToken) {
        configure(currentConfiguration.newAppToken(token))
    }

    override fun getChannelToken(channel: TwitchChannel): AuthToken? {
        return currentConfiguration.getChannelConfiguration(channel)?.authToken
    }

    override suspend fun newChannelToken(channel: TwitchChannel, newToken: AuthToken) {
        configure(currentConfiguration.newChannelToken(channel, newToken))
    }

    fun buildCredentialsManager() = configuration?.let {
        CredentialsManager(
            it.clientId,
            it.clientSecret,
            this
        )
    }

}