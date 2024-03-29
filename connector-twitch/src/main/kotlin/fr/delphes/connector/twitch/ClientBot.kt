package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.ChannelTwitchClient
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.reward.TwitchRewardConfiguration
import fr.delphes.twitch.auth.CredentialsManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ClientBot(
    private val configuration: TwitchConfiguration,
    internal val connector: TwitchConnector,
    val botConfiguration: BotConfiguration,
    val bot: Bot,
    private val credentialsManager: CredentialsManager,
) {
    private val channels = mutableListOf<Channel>()

    //TODO random secret
    private val webhookSecret = "secretWithMoreThan10caracters"

    val twitchApi = AppTwitchClient.build(configuration.clientId, credentialsManager)

    fun register(channel: Channel) {
        channels.add(channel)
    }

    fun channelOf(channel: TwitchChannel): Channel? {
        return channels.firstOrNull { it.channel == channel }
    }

    fun channelApiBuilder(
        botConfiguration: BotConfiguration,
        rewards: List<TwitchRewardConfiguration>,
        channel: TwitchChannel,
    ): ChannelTwitchClient.Builder {
        val user = runBlocking {
            twitchApi.getUserByName(channel.toUser())!!
        }

        return ChannelTwitchClient.builder(
            twitchApi,
            configuration.clientId,
            credentialsManager,
            user,
            botConfiguration.publicUrl,
            botConfiguration.configFilepath,
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
