package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking
import fr.delphes.twitch.api.streamOnline.StreamOnline as StreamOnlineTwitch

class StreamOnlineMapper(
    private val connector: TwitchConnector,
    private val clock: Clock = SystemClock
) : TwitchIncomingEventMapper<StreamOnlineTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.streamOnline.StreamOnline
    ): List<TwitchIncomingEvent> {
        return connector.whenRunning(
            whenRunning = {
                val channel = clientBot.channelOf(twitchEvent.channel)

                //TODO better retrieve
                val stream = runBlocking {
                    channel?.twitchApi?.getStream()
                }

                if (stream == null || channel?.isOnline() == true) {
                    return@whenRunning emptyList()
                }

                val incomingEvent =
                    StreamOnline(twitchEvent.channel, stream.title, clock.now(), stream.game, stream.thumbnailUrl)

                //TODO move to connector implementation
                channel?.state?.changeCurrentStream(
                    Stream(
                        stream.id,
                        incomingEvent.title,
                        incomingEvent.start,
                        incomingEvent.game,
                        stream.thumbnailUrl
                    )
                )

                listOf(incomingEvent)
            },
            whenNotRunning = {
                emptyList()
            }
        )

    }
}