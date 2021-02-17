package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.connector.twitch.command.Command
import fr.delphes.feature.Feature
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
    private val publicUrl: String,
    val configFilepath: String,
    private val features: List<Feature>,
    val bot: Bot,
    private val credentialsManager: CredentialsManager
) {
    val channels = mutableListOf<Channel>()

    //TODO random secret
    private val webhookSecret = "secretWithMoreThan10caracters"

    private val twitchApi = AppTwitchClient.build(configuration.clientId, credentialsManager)

    val ircClient = IrcClient.builder(configuration.botIdentity?.channel!!, credentialsManager).build()

    fun register(channel: Channel) {
        channels.add(channel)
    }

    fun channelOf(channel: TwitchChannel): Channel? {
        //TODO normalize twitch channel name
        return channels.firstOrNull { it.name.equals(channel.name, true) }
    }

    fun commandsFor(channel: TwitchChannel): List<Command> {
        return features
            .filterIsInstance<TwitchFeature>()
            //TODO normalize twitch channel name
            .filter { feature -> feature.channel.name.equals(channel.name, true) }
            .flatMap(TwitchFeature::commands)
    }

    fun connect() {
        ircClient.connect()

        channels.forEach { channel ->
            ircClient.join(IrcChannel.withName(channel.name))
            channel.join()
        }
    }

    fun channelApiBuilder(
        configFilepath: String,
        rewards: List<RewardConfiguration>,
        channel: TwitchChannel
    ): ChannelTwitchClient.Builder {
        val user = runBlocking {
            twitchApi.getUserByName(channel.name)!!
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
