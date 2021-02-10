package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.configuration.BotConfiguration
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.command.Command
import fr.delphes.feature.Feature
import fr.delphes.twitch.AppTwitchClient
import fr.delphes.twitch.ChannelTwitchClient
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ClientBot(
    configuration: BotConfiguration,
    private val publicUrl: String,
    val configFilepath: String,
    private val features: List<Feature>,
    val bot: Bot
) {
    val channels = mutableListOf<Channel>()

    //TODO random secret
    private val webhookSecret = configuration.webhookSecret

    val appCredential = TwitchAppCredential.of(
        configuration.clientId,
        configuration.secretKey,
        tokenRepository = { getToken -> AuthTokenRepository("${configFilepath}\\auth\\bot.json", getToken) }
    )

    private val twitchApi = AppTwitchClient.build(appCredential)

    val ircClient = IrcClient.builder(configuration.botAccountOauth).build()

    fun register(channel: Channel) {
        channels.add(channel)
    }

    fun channelOf(channel: TwitchChannel): Channel? {
        return channels.firstOrNull { it.name == channel.name }
    }

    fun commandsFor(channel: TwitchChannel): List<Command> {
        return features
            .filterIsInstance<TwitchFeature>()
            .filter { feature -> feature.channel == channel }
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
        configuration: ChannelConfiguration,
        channelCredential: TwitchUserCredential,
        configFilepath: String
    ): ChannelTwitchClient.Builder {
        val user = runBlocking {
            twitchApi.getUserByName(configuration.ownerChannel)!!
        }

        return ChannelTwitchClient.builder(
            appCredential,
            channelCredential,
            user,
            publicUrl,
            configFilepath,
            webhookSecret,
            configuration.rewards
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
