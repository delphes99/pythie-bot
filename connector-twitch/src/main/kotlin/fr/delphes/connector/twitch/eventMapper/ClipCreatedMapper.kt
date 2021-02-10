package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.ClipCreated
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.clip.ClipCreated as TwitchClipCreated

class ClipCreatedMapper: TwitchIncomingEventMapper<TwitchClipCreated> {
    override suspend fun handle(twitchEvent: TwitchClipCreated): List<TwitchIncomingEvent> {
        return listOf(
            ClipCreated(
                twitchEvent.channel,
                twitchEvent.clip
            )
        )
    }
}