package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.streamOnline.payload.StreamOnlineEventPayload
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking

//TODO test ?
class StreamOnlineMapper(
    private val connector: TwitchConnector,
    private val clock: Clock = SystemClock
) : TwitchIncomingEventMapper<StreamOnlineEventPayload> {
    override suspend fun handle(
        twitchEvent: StreamOnlineEventPayload
    ): List<TwitchIncomingEvent> {
        return connector.whenRunning(
            whenRunning = {
                val twitchChannel = TwitchChannel(twitchEvent.broadcaster_user_name)
                val channel = clientBot.channelOf(twitchChannel)

                //TODO better retrieve
                val stream = runBlocking {
                    channel?.twitchApi?.getStream()
                }

                if (stream == null || channel?.isOnline() == true) {
                    return@whenRunning emptyList()
                }

                listOf(
                    StreamOnline(
                        twitchChannel,
                        twitchEvent.id,
                        stream.title,
                        clock.now(),
                        stream.game,
                        stream.thumbnailUrl
                    )
                )
            },
            whenNotRunning = {
                emptyList()
            }
        )

    }
}