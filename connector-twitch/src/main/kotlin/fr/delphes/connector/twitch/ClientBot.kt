package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.user.UserCache
import fr.delphes.connector.twitch.user.getUserInfos
import fr.delphes.feature.Feature
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.ChannelTwitchClient
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration

class ClientBot(
    internal val connector: TwitchConnector,
    private val publicUrl: String,
    val configFilepath: String,
    private val features: List<Feature>,
    val bot: Bot,
    private val credentialsManager: CredentialsManager,
) {
    val channels = mutableListOf<Channel>()

    //TODO random secret
    private val webhookSecret = "secretWithMoreThan10caracters"

    val twitchApi = AppTwitchClient.build(connector.configuration.clientId, credentialsManager)
    val userCache = UserCache(
        expirationDuration = Duration.ofMinutes(120),
        clock = SystemClock,
        retrieveUser =  { user, now -> getUserInfos(user, twitchApi, now) }
    )

    lateinit var ircClient: IrcClient

    fun register(channel: Channel) {
        channels.add(channel)
    }

    fun channelOf(channel: TwitchChannel): Channel? {
        return channels.firstOrNull { it.channel == channel }
    }

    fun commandsFor(channel: TwitchChannel): List<Command> {
        return features
            .filterIsInstance<TwitchFeature>()
            .filter { feature -> feature.channel == channel }
            .flatMap(TwitchFeature::commands)
    }

    fun connect() {
        ircClient = IrcClient.builder(connector.configuration.botIdentity?.channel!!, credentialsManager).build()
        ircClient.connect()

        channels.forEach { channel ->
            ircClient.join(IrcChannel.withName(channel.channel.normalizeName))
            channel.join()
        }
    }

    fun channelApiBuilder(
        configFilepath: String,
        rewards: List<RewardConfiguration>,
        channel: TwitchChannel,
    ): ChannelTwitchClient.Builder {
        val user = runBlocking {
            twitchApi.getUserByName(channel.toUser())!!
        }

        return ChannelTwitchClient.builder(
            connector.configuration.clientId,
            credentialsManager,
            user,
            publicUrl,
            configFilepath,
            webhookSecret,
            rewards
        )
    }

    suspend fun resetWebhook() {
        coroutineScope {
            twitchApi.removeAllSubscriptions()

            channels.map {
                launch { it.twitchApi.registerWebhooks() }
            }.joinAll()
        }
        //TODO cron refresh sub
    }
}
