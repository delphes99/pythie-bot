package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.ChannelTwitchClient
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ClientBot(
    private val configuration: TwitchConfiguration,
    internal val connector: TwitchConnector,
    private val publicUrl: String,
    val configFilepath: String,
    val bot: Bot,
    private val credentialsManager: CredentialsManager,
) {
    private val channels = mutableListOf<Channel>()

    //TODO random secret
    private val webhookSecret = "secretWithMoreThan10caracters"

    val twitchApi = AppTwitchClient.build(configuration.clientId, credentialsManager)

    lateinit var ircClient: IrcClient

    fun register(channel: Channel) {
        channels.add(channel)
    }

    fun channelOf(channel: TwitchChannel): Channel? {
        return channels.firstOrNull { it.channel == channel }
    }

    fun connect() {
        ircClient = IrcClient.builder(configuration.botIdentity?.channel!!, credentialsManager).build()
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
            configuration.clientId,
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
