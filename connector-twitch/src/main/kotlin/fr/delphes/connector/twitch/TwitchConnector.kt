package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.outgoingEvent.TwitchOutgoingEvent
import fr.delphes.connector.twitch.webservice.ConfigurationModule
import fr.delphes.connector.twitch.webservice.RewardKtorModule
import fr.delphes.connector.twitch.webservice.WebhookModule
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.TwitchHelixClient
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.twitch.auth.AuthTokenRepository
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class TwitchConnector(
    val bot: Bot,
    override val configFilepath: String,
    val channels: List<ChannelConfiguration>
) : Connector, AuthTokenRepository {
    lateinit var clientBot: ClientBot

    private val repository = TwitchConfigurationRepository("${configFilepath}\\twitch\\configuration.json")
    var configuration = runBlocking { repository.load() }

    internal val credentialsManager = CredentialsManager(
        configuration.clientId,
        configuration.clientSecret,
        this
    )

    private val twitchHelixApi = TwitchHelixClient()
    private val stateMachine = TwitchStateMachine(this)

    init {
        runBlocking {
            stateMachine.on(
                TwitchStateEvent.Configure(
                    repository.load(),
                    this@TwitchConnector
                )
            )
        }
    }

    constructor(
        bot: Bot,
        configFilepath: String,
        vararg channels: ChannelConfiguration
    ) : this(bot, configFilepath, listOf(*channels))

    override fun internalEndpoints(application: Application) {
        application.ConfigurationModule(this)
        application.RewardKtorModule(this)
    }

    override fun publicEndpoints(application: Application) {
        application.WebhookModule(this)
    }

    val botAccount get() = configuration.botAccountName?.let(::TwitchChannel)

    override fun init() {
        this.clientBot = ClientBot(
            this,
            bot.publicUrl,
            bot.configFilepath,
            bot.features,
            bot,
            credentialsManager
        )

        configuration.listenedChannels.forEach { configuredAccount ->
            val legacyChannelConfiguration = channels
                .firstOrNull { channel -> channel.channel == configuredAccount.channel }

            clientBot.register(
                Channel(
                    configuredAccount.channel,
                    legacyChannelConfiguration,
                    credentialsManager,
                    clientBot
                )
            )
        }
    }

    override fun connect() {
        this.stateMachine.on(TwitchStateEvent.Connect(bot))
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
        stateMachine.whenRunning {
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

        return ConfigurationTwitchAccount(this, userInfos.preferred_username.toLowerCase())
    }

    private suspend fun updateConfiguration(newConfiguration: TwitchConfiguration) {
        repository.save(newConfiguration)
        configuration = newConfiguration
    }

    suspend fun <T> whenRunning(
        whenRunning: suspend TwitchState.AppConnected.() -> T,
        whenNotRunning: suspend () -> T,
    ): T {
        return stateMachine.whenRunning(whenRunning, whenNotRunning)
    }

    suspend fun whenRunning(function: suspend TwitchState.AppConnected.() -> Unit) {
        stateMachine.whenRunning(function)
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }

    override fun getAppToken(): AuthToken? {
        return configuration.appToken
    }

    override suspend fun newAppToken(token: AuthToken) {
        updateConfiguration(configuration.newAppToken(token))
    }

    override fun getChannelToken(channel: TwitchChannel): AuthToken? {
        return configuration.getChannelConfiguration(channel)?.authToken
    }

    override suspend fun newChannelToken(channel: TwitchChannel, newToken: AuthToken) {
        updateConfiguration(configuration.newChannelToken(channel, newToken))
    }
}