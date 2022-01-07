package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.connector.twitch.statistics.TwitchStatistics
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.connector.twitch.user.getUserInfos
import fr.delphes.connector.twitch.webservice.ConfigurationModule
import fr.delphes.connector.twitch.webservice.RewardKtorModule
import fr.delphes.connector.twitch.webservice.WebhookModule
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.TwitchHelixClient
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.utils.cache.InMemoryCache
import fr.delphes.utils.time.SystemClock
import io.ktor.application.Application
import java.time.Duration

class TwitchConnector(
    val bot: Bot,
    override val configFilepath: String,
    val channels: List<ChannelConfiguration>
) : Connector<TwitchConfiguration, TwitchRuntime>, AuthTokenRepository {
    override val connectorName = "twitch"

    override val configurationManager = TwitchConfigurationManager(
        TwitchConfigurationRepository("${configFilepath}\\twitch\\configuration.json")
    )

    val statistics = TwitchStatistics(configFilepath)

    private val twitchHelixApi = TwitchHelixClient()

    override val connectorStateManager = TwitchStateManager(this)

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

    fun commandsFor(channel: TwitchChannel): List<Command> {
        return bot.features
            .filterIsInstance<TwitchFeature>()
            .filter { feature -> feature.channel == channel }
            .flatMap(TwitchFeature::commands)

        //TODO editable command
    }

    suspend fun newBotAccountConfiguration(newBotAuth: AuthToken) {
        val account = newBotAuth.toConfigurationTwitchAccount()

        configure(currentConfiguration.setBotAccount(account))
    }

    suspend fun addChannelConfiguration(channelAuth: AuthToken) {
        val account = channelAuth.toConfigurationTwitchAccount()

        configure(currentConfiguration.addChannel(account))
    }

    private val currentConfiguration get() = configurationManager.currentConfiguration

    private suspend fun AuthToken.toConfigurationTwitchAccount(): ConfigurationTwitchAccount {
        val userInfos = twitchHelixApi.getUserInfosOf(this)

        return ConfigurationTwitchAccount(this, userInfos.preferred_username.lowercase())
    }

    private val userCache = InMemoryCache<User, UserInfos>(
        expirationDuration = Duration.ofMinutes(120),
        clock = SystemClock,
        retrieve = { user ->
            connectorStateManager.whenRunning(
                whenRunning = {
                    getUserInfos(user, clientBot.twitchApi)
                },
                whenNotRunning = {
                    null
                }
            )
        }
    )

    suspend fun getUser(user: User): UserInfos? {
        return userCache.getValue(user)
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

    internal suspend fun handleIncomingEvent(event: TwitchIncomingEvent) {
        internalHandler.handle(event)
        bot.handleIncomingEvent(event)
    }

    val botAccount: TwitchChannel?
        get() = currentConfiguration.botAccount
}