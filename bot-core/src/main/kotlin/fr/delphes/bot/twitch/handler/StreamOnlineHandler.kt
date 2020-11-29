package fr.delphes.bot.twitch.handler

import fr.delphes.bot.Channel
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.util.time.Clock
import fr.delphes.bot.util.time.SystemClock
import fr.delphes.twitch.model.Stream
import kotlinx.coroutines.runBlocking
import fr.delphes.twitch.api.streamOnline.StreamOnline as StreamOnlineTwitch

class StreamOnlineHandler(
    private val channel: Channel,
    private val clock: Clock = SystemClock
) : TwitchIncomingEventHandler<StreamOnlineTwitch> {
    override fun handle(
        twitchEvent: StreamOnlineTwitch,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        //TODO better retrieve
        val stream = runBlocking {
            this@StreamOnlineHandler.channel.twitchApi.getStream()
        }
        val incomingEvent = StreamOnline(stream!!.title, clock.now(), stream.game)

        changeState.changeCurrentStream(Stream(incomingEvent.title, incomingEvent.start, incomingEvent.game))

        return listOf(incomingEvent)
    }
}