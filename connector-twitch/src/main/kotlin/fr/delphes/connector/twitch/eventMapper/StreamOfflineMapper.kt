package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.streamOffline.payload.StreamOfflineEventPayload

class StreamOfflineMapper(
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<StreamOfflineEventPayload> {
    override suspend fun handle(
        twitchEvent: StreamOfflineEventPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)
        //TODO move to connector implementation
        connector.whenRunning {
            clientBot.channelOf(channel)?.state?.changeCurrentStream(null)
        }

        return listOf(StreamOffline(channel))
    }
}