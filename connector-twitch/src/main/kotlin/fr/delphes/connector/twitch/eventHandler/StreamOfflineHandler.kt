package fr.delphes.connector.twitch.eventHandler

import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.streamOffline.StreamOffline as StreamOfflineTwitch

class StreamOfflineHandler(
    private val bot: ClientBot
) : TwitchIncomingEventHandler<StreamOfflineTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.streamOffline.StreamOffline
    ): List<TwitchIncomingEvent> {
        bot.channelOf(twitchEvent.channel)?.state?.changeCurrentStream(null)

        return listOf(StreamOffline(twitchEvent.channel))
    }
}