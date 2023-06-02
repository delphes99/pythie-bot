package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorType
import fr.delphes.bot.event.outgoing.OutgoingEventBuilderDefinition
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.connector.twitch.outgoingEvent.ActivateReward
import fr.delphes.connector.twitch.outgoingEvent.DeactivateReward
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.connector.twitch.state.ChannelRewardsState
import fr.delphes.connector.twitch.state.CommandListState
import fr.delphes.connector.twitch.state.GetCurrentStreamState
import fr.delphes.connector.twitch.state.GetUserInfos
import fr.delphes.connector.twitch.state.GetVipState
import fr.delphes.connector.twitch.statistics.TwitchStatistics
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.connector.twitch.user.getUserInfos
import fr.delphes.connector.twitch.webservice.ConfigurationModule
import fr.delphes.connector.twitch.webservice.RewardKtorModule
import fr.delphes.connector.twitch.webservice.WebhookModule
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.TwitchHelixClient
import fr.delphes.twitch.api.channel.ChannelInformation
import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.api.user.UserName
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.utils.cache.InMemoryCache
import fr.delphes.utils.time.SystemClock
import io.ktor.server.application.Application
import java.time.Duration

class TwitchConnector(
    val bot: Bot,
    override val botConfiguration: BotConfiguration,
    val channels: List<ChannelConfiguration>,
) : Connector<TwitchConfiguration, TwitchLegacyRuntime> {
    override val connectorType = ConnectorType("Twitch")

    override val configurationManager = TwitchConfigurationManager(
        TwitchConfigurationRepository(botConfiguration.pathOf("twitch", "configuration.json"))
    )

    override val states = listOf(
        CommandListState(this),
        GetVipState(this),
        GetUserInfos(this),
        GetCurrentStreamState(this),
        //TODO dynamic state (reload the configuration must reload the states)
        *configurationManager.currentConfiguration.listenedChannels.map {
            ChannelRewardsState(it.channel, this)
        }.toTypedArray(),
    )

    val statistics = TwitchStatistics(botConfiguration)

    private val twitchHelixApi = TwitchHelixClient()

    override val connectionManager = TwitchConnectionManager(this)

    override val outgoingEventsTypes: List<OutgoingEventBuilderDefinition> = listOf(
        SendMessage.builderDefinition,
        ActivateReward.builderDefinition,
        DeactivateReward.builderDefinition,
    )

    private val internalHandler = TwitchConnectorHandler(this)

    constructor(
        bot: Bot,
        botConfiguration: BotConfiguration,
        vararg channels: ChannelConfiguration,
    ) : this(bot, botConfiguration, listOf(*channels))

    override fun internalEndpoints(application: Application) {
        application.ConfigurationModule(this)
        application.RewardKtorModule(this)
    }

    override fun publicEndpoints(application: Application) {
        application.WebhookModule(this)
    }

    fun commandsFor(channel: TwitchChannel): List<Command> {
        val commands = bot.featuresManager
            .featureDefinitions
            .filterIsInstance<TwitchFeature>()
            .filter { feature -> feature.channel == channel }
            .flatMap(TwitchFeature::commands)
        //TODO : delete
        val legacyCommands = bot.legacyfeatures
            .filterIsInstance<TwitchFeature>()
            .filter { feature -> feature.channel == channel }
            .flatMap(TwitchFeature::commands)

        return commands + legacyCommands
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

        return ConfigurationTwitchAccount.of(this, userInfos.preferred_username.lowercase())
    }

    private val userCache = InMemoryCache<String, UserInfos>(
        expirationDuration = Duration.ofMinutes(120),
        clock = SystemClock,
        retrieve = { user ->
            connectionManager.whenRunning(
                whenRunning = {
                    getUserInfos(UserName(user), clientBot.twitchApi)
                },
                //TODO do not set cache value if not connected
                whenNotRunning = {
                    null
                }
            )
        }
    )

    private val channelInformationCache = InMemoryCache<UserId, ChannelInformation>(
        expirationDuration = Duration.ofMinutes(5),
        clock = SystemClock,
        retrieve = { user ->
            connectionManager.whenRunning(
                whenRunning = {
                    clientBot.twitchApi.getChannelInformation(user)
                },
                whenNotRunning = {
                    null
                }
            )
        }
    )

    suspend fun getUser(user: UserName): UserInfos? {
        return userCache.getValue(user.normalizeName)
    }

    suspend fun getChannelInformation(user: UserId): ChannelInformation? {
        return channelInformationCache.getValue(user)
    }

    internal suspend fun handleIncomingEvent(event: TwitchIncomingEvent) {
        internalHandler.handle(event)
        bot.handle(event)
    }

    val botAccount: TwitchChannel?
        get() = currentConfiguration.botAccount
}