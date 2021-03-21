package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.streamOffline.StreamOffline as StreamOfflineTwitch

class StreamOfflineMapper(
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<StreamOfflineTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.streamOffline.StreamOffline
    ): List<TwitchIncomingEvent> {
        //TODO move to connector implementation
        connector.whenRunning {
            clientBot.channelOf(twitchEvent.channel)?.state?.changeCurrentStream(null)
        }

        return listOf(StreamOffline(twitchEvent.channel))
    }
}