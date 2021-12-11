package fr.delphes.connector.twitch

import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.eventMapper.ChannelMessageMapper
import fr.delphes.connector.twitch.eventMapper.ClipCreatedMapper
import fr.delphes.connector.twitch.eventMapper.IRCMessageMapper
import fr.delphes.connector.twitch.eventMapper.TwitchIncomingEventMapper
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.auth.CredentialsManager
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import kotlinx.coroutines.runBlocking

class Channel(
    val channel: TwitchChannel,
    configuration: ChannelConfiguration?,
    credentialsManager: CredentialsManager,
    val bot: ClientBot,
    val connector: TwitchConnector,
) {
    //TODO move irc client to twitch API
    val ircClient = IrcClient.builder(channel, credentialsManager)
        .withOnMessage { message ->
            runBlocking {
                IRCMessageMapper(channel).handleTwitchEvent(message)
            }
        }
        .withOnChannelMessage { message ->
            runBlocking {
                ChannelMessageMapper(channel, connector).handleTwitchEvent(message)
            }
        }
        .build()

    val twitchApi: ChannelTwitchApi

    init {
        val clipCreatedMapper = ClipCreatedMapper()

        //TODO subscribe only when feature requires
        twitchApi =
            bot.channelApiBuilder(bot.configFilepath, configuration?.rewards ?: emptyList(), channel)
                .listenToReward()
                .listenToNewFollow()
                .listenToNewSub()
                .listenToNewCheer()
                .listenToStreamOnline()
                .listenToStreamOffline()
                .listenToChannelUpdate()
                .listenToIncomingRaid()
                .listenToPrediction()
                .listenToPoll()
                .listenToClipCreated { clipCreatedMapper.handleTwitchEvent(it) }
                .build()


        runBlocking {
            //TODO move into connector ?
            connector.statistics.of(channel).init(twitchApi.getStream())

            //TODO Synchronize reward
        }
    }

    private fun <T> TwitchIncomingEventMapper<T>.handleTwitchEvent(request: T) {
        //TODO make suspendable
        runBlocking {
            this@handleTwitchEvent.handle(request).forEach { incomingEvent ->
                connector.handleIncomingEvent(incomingEvent)
            }
        }
    }

    fun join() {
        ircClient.connect()
        ircClient.join(IrcChannel.of(channel))
    }
}