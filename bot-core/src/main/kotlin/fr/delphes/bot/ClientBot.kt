package fr.delphes.bot

import fr.delphes.bot.command.Command
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.configuration.BotConfiguration
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.feature.Feature
import fr.delphes.feature.TwitchFeature
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
    val connectors: List<Connector>,
    private val features: List<Feature>
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

    //TODO move to connector
    fun commandsFor(channel: TwitchChannel): List<Command> {
        return features
            .filterIsInstance<TwitchFeature>()
            .filter { feature -> feature.channel == channel }
            .flatMap(TwitchFeature::commands)
    }

    //TODO move to connector
    fun connect() {
        ircClient.connect()

        channels.forEach { channel ->
            ircClient.join(IrcChannel.withName(channel.name))
            channel.join()
        }
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

    fun channelApiBuilder(
        configuration: ChannelConfiguration,
        channelCredential: TwitchUserCredential
    ): ChannelTwitchClient.Builder {
        val user = runBlocking {
            twitchApi.getUserByName(configuration.ownerChannel)!!
        }

        return ChannelTwitchClient.builder(
            appCredential,
            channelCredential,
            user,
            publicUrl,
            webhookSecret,
            configuration.rewards
        )
    }

    suspend fun handleEvent(incomingEvent: IncomingEvent) {
        //TODO move channel into event / target the good channel
        channels.forEach { channel ->
            channel.handleIncomingEvent(incomingEvent)
        }
    }
}
