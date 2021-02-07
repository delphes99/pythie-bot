package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.outgoingEvent.TwitchOutgoingEvent
import fr.delphes.connector.twitch.webservice.ConfigurationModule
import fr.delphes.connector.twitch.webservice.WebhookModule
import fr.delphes.twitch.TwitchHelixClient
import fr.delphes.twitch.auth.AuthToken
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class TwitchConnector(
    override val configFilepath: String,
    val channels: List<ChannelConfiguration>
) : Connector {
    private lateinit var bot: Bot
    lateinit var clientBot: ClientBot

    private val repository = TwitchConfigurationRepository("${configFilepath}\\twitch\\configuration.json")
    internal var configuration: TwitchConfiguration = runBlocking { repository.load() }
    private val twitchHelixApi = TwitchHelixClient()
    private var state: TwitchState = TwitchState.Unconfigured

    init {
        runBlocking {
            state = TwitchState.Unconfigured.configure(repository.load(), configFilepath)
        }
    }

    constructor(
        configFilepath: String,
        vararg channels: ChannelConfiguration
    ) : this(configFilepath, listOf(*channels))

    override fun internalEndpoints(application: Application) {
        application.ConfigurationModule(this)
    }

    override fun publicEndpoints(application: Application) {
        application.WebhookModule(this)
    }

    override fun init(bot: Bot) {
        this.bot = bot
        this.clientBot = ClientBot(
            bot.configuration,
            bot.publicUrl,
            bot.configFilepath,
            bot.features,
            bot
        )

        channels.forEach { channelConfiguration ->
            clientBot.register(
                Channel(
                    channelConfiguration,
                    clientBot
                )
            )
        }
    }

    override fun connect() {
        val oldState = state
        this.state = when (oldState) {
            is TwitchState.AppConfigurationFailed -> oldState
            is TwitchState.AppConfigured -> oldState.connect(bot)
            is TwitchState.AppConnected -> oldState
            TwitchState.Unconfigured -> oldState
        }
    }

    override suspend fun execute(event: OutgoingEvent) {
        if (event is TwitchOutgoingEvent) {
            val channel = clientBot.channelOf(event.channel)!!
            try {
                event.executeOnTwitch(clientBot.ircClient, channel.ircClient, channel.twitchApi, channel)
            } catch (e: Exception) {
                LOGGER.error(e) { "Error while handling event ${e.message}" }
            }
        }
    }

    override suspend fun resetWebhook() {
        state.whenRunning {
            clientBot.resetWebhook()
        }
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

    suspend fun <T> whenRunning(
        whenRunning: suspend TwitchState.AppConnected.() -> T,
        whenNotRunning: suspend () -> T,
    ): T {
        val currentState = state
        return if (currentState is TwitchState.AppConnected) {
            currentState.whenRunning()
        } else {
            whenNotRunning()
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}