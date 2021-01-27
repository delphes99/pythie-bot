package fr.delphes.connector.twitch

import fr.delphes.bot.Channel
import fr.delphes.bot.ClientBot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.webservice.AuthModule
import fr.delphes.connector.twitch.webservice.ConfigurationModule
import fr.delphes.twitch.TwitchHelixClient
import fr.delphes.twitch.auth.AuthToken
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking

class TwitchConnector(
    override val configFilepath: String,
    private val channels: List<ChannelConfiguration>
) : Connector {
    private val repository = TwitchConfigurationRepository("${configFilepath}\\twitch\\configuration.json")
    internal var configuration: TwitchConfiguration = runBlocking { repository.load() }
    private val twitchHelixApi = TwitchHelixClient()
    private var state: TwitchState = TwitchState.Unconfigured

    constructor(
        configFilepath: String,
        vararg channels: ChannelConfiguration
    ) : this(configFilepath, listOf(*channels))

    override fun initChannel(bot: ClientBot) {
        channels.forEach { channelConfiguration ->
            bot.register(Channel(channelConfiguration, bot))
        }
    }

    override fun internalEndpoints(application: Application, bot: ClientBot) {
        application.ConfigurationModule(this)
    }

    override fun publicEndpoints(application: Application, bot: ClientBot) {
        application.AuthModule(channels)
    }

    override fun connect(bot: ClientBot) {
        bot.connect()
    }

    override suspend fun execute(event: OutgoingEvent) {
    }

    suspend fun configureAppCredential(clientId: String, clientSecret: String) {
        updateConfiguration(configuration.setAppCredential(clientId, clientSecret))
    }

    suspend fun newBotAccountConfiguration(newBotAuth: AuthToken) {
        val account = newBotAuth.toConfigurationTwitchAccount()

        updateConfiguration(configuration.setBotAccount(account))
    }

    suspend fun addChannelConfiguration(channelAuth: AuthToken) {
        val account = channelAuth.toConfigurationTwitchAccount()

        updateConfiguration(configuration.addChannel(account))
    }

    suspend fun removeChannel(channelName: String) {
        updateConfiguration(configuration.removeChannel(channelName))
    }

    private suspend fun AuthToken.toConfigurationTwitchAccount(): ConfigurationTwitchAccount {
        val userInfos = twitchHelixApi.getUserInfosOf(this)

        return ConfigurationTwitchAccount(this, userInfos.preferred_username)
    }

    private suspend fun updateConfiguration(newConfiguration: TwitchConfiguration) {
        repository.save(newConfiguration)
        configuration = newConfiguration
    }
}