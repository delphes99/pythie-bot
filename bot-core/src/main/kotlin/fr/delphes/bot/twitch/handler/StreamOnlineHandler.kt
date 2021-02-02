package fr.delphes.bot.twitch.handler

import fr.delphes.bot.Channel
import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.event.incoming.TwitchIncomingEvent
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking
import fr.delphes.twitch.api.streamOnline.StreamOnline as StreamOnlineTwitch

class StreamOnlineHandler(
    private val channel: Channel,
    private val bot: ClientBot,
    private val clock: Clock = SystemClock
) : TwitchIncomingEventHandler<StreamOnlineTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.streamOnline.StreamOnline
    ): List<TwitchIncomingEvent> {
        //TODO better retrieve
        val stream = runBlocking {
            this@StreamOnlineHandler.channel.twitchApi.getStream()
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