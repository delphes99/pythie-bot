package fr.delphes.bot

import fr.delphes.bot.command.Command
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.TwitchOutgoingEvent
import fr.delphes.bot.state.ChannelState
import fr.delphes.bot.state.FileStatisticsRepository
import fr.delphes.bot.state.Statistics
import fr.delphes.bot.state.StreamStatistics
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.twitch.handler.ChannelBitsHandler
import fr.delphes.bot.twitch.handler.ChannelMessageHandler
import fr.delphes.bot.twitch.handler.ChannelUpdateHandler
import fr.delphes.bot.twitch.handler.IRCMessageHandler
import fr.delphes.bot.twitch.handler.NewFollowHandler
import fr.delphes.bot.twitch.handler.NewSubHandler
import fr.delphes.bot.twitch.handler.RewardRedeemedHandler
import fr.delphes.bot.twitch.handler.StreamOfflineHandler
import fr.delphes.bot.twitch.handler.StreamOnlineHandler
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.feature.TwitchFeature
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import fr.delphes.utils.exhaustive
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class Channel(
    configuration: ChannelConfiguration,
    val bot: ClientBot,
    private val state: ChannelState = ChannelState(FileStatisticsRepository("${bot.configFilepath}\\${configuration.ownerChannel}"))
) : ChannelInfo {
    override val commands: List<Command> = configuration.features
        .filterIsInstance<TwitchFeature>()
        .flatMap(TwitchFeature::commands)
    override val currentStream: Stream? get() = state.currentStream
    override val statistics: Statistics get() = state.statistics
    override val streamStatistics: StreamStatistics? get() = state.streamStatistics
    val alerts = Channel<Alert>()

    val name = configuration.ownerChannel
    private val channelCredential = TwitchUserCredential.of(
        bot.appCredential,
        AuthTokenRepository(
            "${bot.configFilepath}\\auth\\channel-$name.json"
        )
    )

    private val oAuth = configuration.ownerAccountOauth

    val features = configuration.features
    private val eventHandlers = EventHandlers()

    //TODO move irc client to twitch API
    private val ircClient = IrcClient.builder(oAuth)
        .withOnMessage { message -> IRCMessageHandler(TwitchChannel(name)).handleTwitchEvent(message) }
        .withOnChannelMessage { message -> ChannelMessageHandler(TwitchChannel(name)).handleTwitchEvent(message) }
        .build()

    val twitchApi: ChannelTwitchApi

    //TODO subscribe only when feature requires
    init {
        twitchApi =
            bot.channelApiBuilder(configuration, channelCredential)
                .listenToReward { RewardRedeemedHandler().handleTwitchEvent(it) }
                .listenToNewFollow { NewFollowHandler().handleTwitchEvent(it) }
                .listenToNewSub { NewSubHandler().handleTwitchEvent(it) }
                .listenToNewCheer { ChannelBitsHandler().handleTwitchEvent(it) }
                .listenToStreamOnline { StreamOnlineHandler(this).handleTwitchEvent(it) }
                .listenToStreamOffline { StreamOfflineHandler().handleTwitchEvent(it) }
                .listenToChannelUpdate { ChannelUpdateHandler().handleTwitchEvent(it) }
                .build()

        features.forEach { feature ->
            feature.registerHandlers(eventHandlers)
        }

        runBlocking {
            state.init(twitchApi.getStream())

            //TODO Synchronize reward
        }
    }

    private fun <T> TwitchIncomingEventHandler<T>.handleTwitchEvent(request: T) {
        //TODO make suspendable
        runBlocking {
            this@handleTwitchEvent.handle(request, this@Channel, this@Channel.state).forEach { incomingEvent ->
                handleIncomingEvent(incomingEvent)
            }
        }
    }

    suspend fun handleIncomingEvent(incomingEvent: IncomingEvent) {
        eventHandlers.handleEvent(incomingEvent, this@Channel).execute()
    }

    private suspend fun List<OutgoingEvent>.execute() {
        coroutineScope {
            forEach { e ->
                @Suppress("IMPLICIT_CAST_TO_ANY")
                when (e) {
                    is TwitchOutgoingEvent -> {
                        try {
                            e.executeOnTwitch(bot.ircClient, ircClient, twitchApi, this@Channel)
                        } catch (e: Exception) {
                            LOGGER.error(e) { "Error while handling event ${e.message}" }
                        }
                    }
                    is Alert -> {
                        launch {
                            alerts.send(e)
                        }
                    }
                    else -> {
                        bot.connectors.forEach { it.execute(e) }
                    }
                }.exhaustive()
            }
        }
    }

    suspend fun newAuth(auth: AuthToken) {
        LOGGER.info { "Save new credentials for channel : $name" }
        this.channelCredential.newAuth(auth)
    }

    fun join() {
        ircClient.connect()
        ircClient.join(IrcChannel(name))
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}
