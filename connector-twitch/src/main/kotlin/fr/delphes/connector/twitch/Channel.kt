package fr.delphes.connector.twitch

import fr.delphes.bot.state.ChannelState
import fr.delphes.bot.state.FileStatisticsRepository
import fr.delphes.bot.state.Statistics
import fr.delphes.bot.state.StreamStatistics
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.eventMapper.ChannelBitsMapper
import fr.delphes.connector.twitch.eventMapper.ChannelMessageMapper
import fr.delphes.connector.twitch.eventMapper.ChannelUpdateMapper
import fr.delphes.connector.twitch.eventMapper.IRCMessageMapper
import fr.delphes.connector.twitch.eventMapper.NewFollowMapper
import fr.delphes.connector.twitch.eventMapper.NewSubMapper
import fr.delphes.connector.twitch.eventMapper.RewardRedeemedMapper
import fr.delphes.connector.twitch.eventMapper.StreamOfflineMapper
import fr.delphes.connector.twitch.eventMapper.StreamOnlineMapper
import fr.delphes.connector.twitch.eventMapper.TwitchIncomingEventMapper
import fr.delphes.connector.twitch.incomingEvent.ClipCreated
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.twitch.auth.AuthTokenRepository
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import fr.delphes.utils.scheduler.Scheduler
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.LocalDateTime

class Channel(
    configuration: ChannelConfiguration,
    val bot: ClientBot,
    val state: ChannelState = ChannelState(FileStatisticsRepository("${bot.configFilepath}\\${configuration.ownerChannel}"))
) {
    val currentStream: Stream? get() = state.currentStream
    val statistics: Statistics get() = state.statistics
    val streamStatistics: StreamStatistics? get() = state.streamStatistics

    fun isOnline(): Boolean = currentStream != null

    val name = configuration.ownerChannel
    private val channelCredential = TwitchUserCredential.of(
        bot.appCredential,
        AuthTokenRepository(
            "${bot.configFilepath}\\auth\\channel-$name.json"
        )
    )

    private val oAuth = configuration.ownerAccountOauth

    //TODO move irc client to twitch API
    val ircClient = IrcClient.builder(oAuth)
        .withOnMessage { message ->
            runBlocking {
                IRCMessageMapper(TwitchChannel(name)).handleTwitchEvent(message)
            }
        }
        .withOnChannelMessage { message ->
            runBlocking {
                ChannelMessageMapper(TwitchChannel(name), bot).handleTwitchEvent(message)
            }
        }
        .build()


    private val schedulerForClips = Scheduler(Duration.ofSeconds(20)) {
        val startedAfter = LocalDateTime.of(2021, 2, 1, 0, 0, 0)

        twitchApi.getClips(startedAfter)
            .takeLast(1).map { clip -> ClipCreated(TwitchChannel(name), clip) }.forEach { event ->
                bot.bot.handleIncomingEvent(event)
            }
    }

    val twitchApi: ChannelTwitchApi

    //TODO subscribe only when feature requires
    init {
        val rewardRedeemedMapper = RewardRedeemedMapper()
        val newFollowMapper = NewFollowMapper(bot)
        val newSubMapper = NewSubMapper(bot)
        val channelBitsMapper = ChannelBitsMapper(bot)
        val streamOnlineMapper = StreamOnlineMapper(this, bot)
        val streamOfflineMapper = StreamOfflineMapper(bot)
        val channelUpdateMapper = ChannelUpdateMapper(bot)

        twitchApi =
            bot.channelApiBuilder(configuration, channelCredential)
                .listenToReward { rewardRedeemedMapper.handleTwitchEvent(it) }
                .listenToNewFollow { newFollowMapper.handleTwitchEvent(it) }
                .listenToNewSub { newSubMapper.handleTwitchEvent(it) }
                .listenToNewCheer { channelBitsMapper.handleTwitchEvent(it) }
                .listenToStreamOnline { streamOnlineMapper.handleTwitchEvent(it) }
                .listenToStreamOffline { streamOfflineMapper.handleTwitchEvent(it) }
                .listenToChannelUpdate { channelUpdateMapper.handleTwitchEvent(it) }
                .build()


        runBlocking {
            state.init(twitchApi.getStream())

            //TODO Synchronize reward
        }

        //TODO finish clip created handler schedulerForClips.start()
    }

    private fun <T> TwitchIncomingEventMapper<T>.handleTwitchEvent(request: T) {
        //TODO make suspendable
        runBlocking {
            this@handleTwitchEvent.handle(request).forEach { incomingEvent ->
                bot.bot.handleIncomingEvent(incomingEvent)
            }
        }
    }

    fun join() {
        ircClient.connect()
        ircClient.join(IrcChannel.withName(name))
    }
}