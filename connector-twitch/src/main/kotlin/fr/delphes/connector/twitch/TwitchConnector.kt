package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConfigurationManager
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.initStateMachine
import fr.delphes.bot.connector.state.Connected
import fr.delphes.bot.connector.state.ConnectionSuccessful
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchApiOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchChatOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchOutgoingEvent
import fr.delphes.connector.twitch.outgoingEvent.TwitchOwnerChatOutgoingEvent
import fr.delphes.connector.twitch.statistics.TwitchStatistics
import fr.delphes.connector.twitch.webservice.ConfigurationModule
import fr.delphes.connector.twitch.webservice.RewardKtorModule
import fr.delphes.connector.twitch.webservice.WebhookModule
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.TwitchHelixClient
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.CredentialsManager
import io.ktor.application.Application
import mu.KotlinLogging

class TwitchConnector(
    val bot: Bot,
    override val configFilepath: String,
    val channels: List<ChannelConfiguration>
) : Connector<TwitchConfiguration, TwitchRuntime>, AuthTokenRepository {
    override val connectorName = "twitch"

    override val configurationManager = ConfigurationManager(
        TwitchConfigurationRepository("${configFilepath}\\twitch\\configuration.json")
    )

    val statistics = TwitchStatistics(configFilepath)

    private val twitchHelixApi = TwitchHelixClient()
    override val connectorStateManager = initStateMachine { configuration, _ ->
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
    }

    private val internalHandler = TwitchConnectorHandler(this)

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
            val currentState = connectorStateManager.state
            if (currentState is Connected) {
                val clientBot = currentState.runtime.clientBot
                try {
                    when (event) {
                        is TwitchApiOutgoingEvent -> {
                            val channel = clientBot.channelOf(event.channel)!!
                            event.executeOnTwitch(channel.twitchApi)
                        }
                        is TwitchChatOutgoingEvent -> {
                            event.executeOnTwitch(clientBot.ircClient)
                        }
                        is TwitchOwnerChatOutgoingEvent -> {
                            val channel = clientBot.channelOf(event.channel)!!
                            event.executeOnTwitch(channel.ircClient)
                        }
                    }
                } catch (e: Exception) {
                    LOGGER.error(e) { "Error while handling event ${e.message}" }
                }
            }
        }
    }

    fun commandsFor(channel: TwitchChannel): List<Command> {
        return bot.features
            .filterIsInstance<TwitchFeature>()
            .filter { feature -> feature.channel == channel }
            .flatMap(TwitchFeature::commands)

        //TODO editable command
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

    private fun currentConfiguration() = configurationManager.configuration ?: TwitchConfiguration.empty

    private suspend fun AuthToken.toConfigurationTwitchAccount(): ConfigurationTwitchAccount {
        val userInfos = twitchHelixApi.getUserInfosOf(this)

        return ConfigurationTwitchAccount(this, userInfos.preferred_username.lowercase())
    }

    suspend fun <T> whenRunning(
        whenRunning: suspend TwitchRuntime.() -> T,
        whenNotRunning: suspend () -> T,
    ): T {
        val currentState = connectorStateManager.state
        return if (currentState is Connected) {
            currentState.runtime.whenRunning()
        } else {
            whenNotRunning()
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

    val botAccount: TwitchChannel?
        get() = currentConfiguration().botAccount
}