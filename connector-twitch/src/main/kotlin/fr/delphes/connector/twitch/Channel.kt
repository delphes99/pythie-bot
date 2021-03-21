package fr.delphes.connector.twitch

import fr.delphes.bot.state.ChannelState
import fr.delphes.bot.state.FileStatisticsRepository
import fr.delphes.bot.state.Statistics
import fr.delphes.bot.state.StreamStatistics
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.eventMapper.ChannelMessageMapper
import fr.delphes.connector.twitch.eventMapper.ClipCreatedMapper
import fr.delphes.connector.twitch.eventMapper.IRCMessageMapper
import fr.delphes.connector.twitch.eventMapper.TwitchIncomingEventMapper
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import kotlinx.coroutines.runBlocking

class Channel(
    val channel: TwitchChannel,
    configuration: ChannelConfiguration?,
    credentialsManager: CredentialsManager,
    val bot: ClientBot,
    val state: ChannelState = ChannelState(FileStatisticsRepository("${bot.configFilepath}\\${channel.normalizeName}"))
) {
    val currentStream: Stream? get() = state.currentStream
    val statistics: Statistics get() = state.statistics
    val streamStatistics: StreamStatistics? get() = state.streamStatistics

    fun isOnline(): Boolean = currentStream != null

    //TODO move irc client to twitch API
    val ircClient = IrcClient.builder(channel, credentialsManager)
        .withOnMessage { message ->
            runBlocking {
                IRCMessageMapper(channel).handleTwitchEvent(message)
            }
        }
        .withOnChannelMessage { message ->
            runBlocking {
                ChannelMessageMapper(channel, bot).handleTwitchEvent(message)
            }
        }
        .build()

    val twitchApi: ChannelTwitchApi

    //TODO subscribe only when feature requires
    init {
        val clipCreatedMapper = ClipCreatedMapper()

        twitchApi =
            bot.channelApiBuilder(bot.configFilepath, configuration?.rewards ?: emptyList(), channel)
                .listenToReward { }
                .listenToNewFollow { }
                .listenToNewSub { }
                .listenToNewCheer { }
                .listenToStreamOnline { }
                .listenToStreamOffline { }
                .listenToChannelUpdate { }
                .listenToClipCreated { clipCreatedMapper.handleTwitchEvent(it) }
                .build()


        runBlocking {
            state.init(twitchApi.getStream())

            //TODO Synchronize reward
        }
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
        ircClient.join(IrcChannel.of(channel))
    }
}