package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.streamOffline.payload.StreamOfflineCondition
import io.kotest.core.spec.style.ShouldSpec

class StreamOfflineMapperTest : ShouldSpec({
    should("map to incomingEvent") {
        "/eventsub/payload/stream.offline.json"
            .shouldBeMappedTo(
                StreamOfflineMapper(),
                StreamOfflineCondition::class,
                StreamOffline(
                    TwitchChannel("Cool_User")
                )
            )
    }
})