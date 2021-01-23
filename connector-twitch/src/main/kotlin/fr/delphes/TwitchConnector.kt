package fr.delphes

import fr.delphes.bot.Channel
import fr.delphes.bot.ClientBot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.twitch.TwitchHelixClient
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.webservice.AuthModule
import fr.delphes.webservice.ConfigurationModule
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking

class TwitchConnector(
    configFilepath: String,
    private val channels: List<ChannelConfiguration>
) : Connector {
    private val repository = TwitchConfigurationRepository("${configFilepath}\\twitch\\configuration.json")
    internal var configuration: TwitchConfiguration = runBlocking { repository.load() }
    private val twitchHelixApi = TwitchHelixClient()

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
        val newConfiguration = configuration.copy(
            clientId = clientId,
            clientSecret = clientSecret
        )
        repository.save(newConfiguration)
        configuration = newConfiguration
    }

    suspend fun newBotAccountConfiguration(newBotAuth: AuthToken) {
        val userInfos = twitchHelixApi.getUserInfosOf(newBotAuth)

        val newConfiguration = configuration.copy(
            botAccountCredential = ConfigurationTwitchAccount(newBotAuth, userInfos.preferred_username)
        )

        repository.save(newConfiguration)
        configuration = newConfiguration
    }
}