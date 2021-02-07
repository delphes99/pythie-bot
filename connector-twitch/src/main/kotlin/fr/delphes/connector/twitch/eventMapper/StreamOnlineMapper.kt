package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.Channel
import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking
import fr.delphes.twitch.api.streamOnline.StreamOnline as StreamOnlineTwitch

class StreamOnlineMapper(
    private val channel: Channel,
    private val bot: ClientBot,
    private val clock: Clock = SystemClock
) : TwitchIncomingEventMapper<StreamOnlineTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.streamOnline.StreamOnline
    ): List<TwitchIncomingEvent> {
        //TODO better retrieve
        val stream = runBlocking {
            this@StreamOnlineMapper.channel.twitchApi.getStream()
        }

        if (bot.channelOf(twitchEvent.channel)?.isOnline() == true) {
            return emptyList()
        }

        val incomingEvent =
            StreamOnline(twitchEvent.channel, stream!!.title, clock.now(), stream.game, stream.thumbnailUrl)

        bot.channelOf(twitchEvent.channel)?.state?.changeCurrentStream(
            Stream(
                stream.id,
                incomingEvent.title,
                incomingEvent.start,
                incomingEvent.game,
                stream.thumbnailUrl
            )
        )

        return listOf(incomingEvent)
    }
}