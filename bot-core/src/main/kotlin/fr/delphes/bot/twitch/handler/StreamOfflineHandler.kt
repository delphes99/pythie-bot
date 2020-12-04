package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.streamOffline.StreamOffline as StreamOfflineTwitch

class StreamOfflineHandler : TwitchIncomingEventHandler<StreamOfflineTwitch> {
    override suspend fun handle(
        twitchEvent: StreamOfflineTwitch,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        changeState.changeCurrentStream(null)

        return listOf(StreamOffline)
    }
}