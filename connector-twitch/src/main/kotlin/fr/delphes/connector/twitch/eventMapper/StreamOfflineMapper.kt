package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.streamOffline.payload.StreamOfflineEventPayload

class StreamOfflineMapper : TwitchIncomingEventMapper<StreamOfflineEventPayload> {
    override suspend fun handle(
        twitchEvent: StreamOfflineEventPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)

        return listOf(
            StreamOffline(channel)
        )
    }
}