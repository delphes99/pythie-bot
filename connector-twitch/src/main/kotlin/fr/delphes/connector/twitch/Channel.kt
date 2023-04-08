package fr.delphes.connector.twitch

import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.twitch.eventMapper.ClipCreatedMapper
import fr.delphes.connector.twitch.eventMapper.TwitchIncomingEventMapper
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import kotlinx.coroutines.runBlocking

class Channel(
    val channel: TwitchChannel,
    configuration: ChannelConfiguration?,
    val bot: ClientBot,
    val connector: TwitchConnector,
) {
    val twitchApi: ChannelTwitchApi

    init {
        val clipCreatedMapper = ClipCreatedMapper()

        //TODO subscribe only when feature requires
        twitchApi =
            bot.channelApiBuilder(bot.botConfiguration, configuration?.rewards ?: emptyList(), channel)
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
}