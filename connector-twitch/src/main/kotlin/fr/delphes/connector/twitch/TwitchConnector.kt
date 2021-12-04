package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchOutgoingEvent
import fr.delphes.connector.twitch.state.BotAccountProvider
import fr.delphes.connector.twitch.webservice.ConfigurationModule
import fr.delphes.connector.twitch.webservice.RewardKtorModule
import fr.delphes.connector.twitch.webservice.WebhookModule
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.TwitchHelixClient
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.CredentialsManager
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class TwitchConnector(
    val bot: Bot,
    override val configFilepath: String,
    val channels: List<ChannelConfiguration>
) : Connector<TwitchConfiguration, TwitchRuntime>, AuthTokenRepository, BotAccountProvider {
    override val connectorName = "twitch"
    private val repository = TwitchConfigurationRepository("${configFilepath}\\twitch\\configuration.json")

    private val twitchHelixApi = TwitchHelixClient()
    override val stateMachine = ConnectorStateMachine<TwitchConfiguration, TwitchRuntime>(
        repository = repository,
        doConnection = { configuration, _ ->
            val credentialsManager = CredentialsManager(
                configuration.clientId,
                configuration.clientSecret,
                this@TwitchConnector
            )

            val clientBot = ClientBot(
                configuration,
                this@TwitchConnector,
                this@TwitchConnector.bot.publicUrl,
                this@TwitchConnector.bot.configFilepath,
                this@TwitchConnector.bot.features,
                this@TwitchConnector.bot,
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
                        clientBot,
                        this@TwitchConnector
                    )
                )
            }

            clientBot.connect()

            clientBot.resetWebhook()

            ConnectionSuccessful(
                configuration,
                TwitchRuntime(
                    configuration,
                    clientBot
                )
            )
        },
        doDisconnect = { _, _ ->
            TODO()
        }
    )
    private val internalHandler = TwitchConnectorHandler(this)

    init {
        runBlocking {
            stateMachine.load()
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

    override suspend fun execute(event: OutgoingEvent) {
        if (event is TwitchOutgoingEvent) {
            whenRunning {
                val channel = clientBot.channelOf(event.channel)!!
                try {
                    event.executeOnTwitch(clientBot.ircClient, channel.ircClient, channel.twitchApi, channel)
                } catch (e: Exception) {
                    LOGGER.error(e) { "Error while handling event ${e.message}" }
                }
            }
        }
    }

    suspend fun configureAppCredential(clientId: String, clientSecret: String) {
        configure(currentConfiguration().setAppCredential(clientId, clientSecret))
    }

    suspend fun newBotAccountConfiguration(newBotAuth: AuthToken) {
        val account = newBotAuth.toConfigurationTwitchAccount()

        configure(currentConfiguration().setBotAccount(account))
    }

    suspend fun addChannelConfiguration(channelAuth: AuthToken) {
        val account = channelAuth.toConfigurationTwitchAccount()

        configure(currentConfiguration().addChannel(account))
    }

    suspend fun removeChannel(channelName: String) {
        configure(currentConfiguration().removeChannel(channelName))
    }

    private fun currentConfiguration() = stateMachine.state.configuration ?: TwitchConfiguration.empty

    private suspend fun AuthToken.toConfigurationTwitchAccount(): ConfigurationTwitchAccount {
        val userInfos = twitchHelixApi.getUserInfosOf(this)

        return ConfigurationTwitchAccount(this, userInfos.preferred_username.lowercase())
    }

    suspend fun <T> whenRunning(
        whenRunning: suspend TwitchRuntime.() -> T,
        whenNotRunning: suspend () -> T,
    ): T {
        val currentState = stateMachine.state
        return if (currentState is Connected) {
            currentState.runtime.whenRunning()
        } else {
            whenNotRunning()
        }
    }

    suspend fun whenRunning(doStuff: suspend TwitchRuntime.() -> Unit) {
        val currentState = stateMachine.state
        if (currentState is Connected) {
            currentState.runtime.doStuff()
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }

    override fun getAppToken(): AuthToken? {
        return currentConfiguration().appToken
    }

    override suspend fun newAppToken(token: AuthToken) {
        configure(currentConfiguration().newAppToken(token))
    }

    override fun getChannelToken(channel: TwitchChannel): AuthToken? {
        return currentConfiguration().getChannelConfiguration(channel)?.authToken
    }

    override suspend fun newChannelToken(channel: TwitchChannel, newToken: AuthToken) {
        configure(currentConfiguration().newChannelToken(channel, newToken))
    }

    internal suspend fun handleIncomingEvent(event: TwitchIncomingEvent) {
        internalHandler.handle(event)
        bot.handleIncomingEvent(event)
    }

    override val botAccount: TwitchChannel?
        get() = currentConfiguration().botAccount
}