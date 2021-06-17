package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.streamOffline.payload.StreamOfflineCondition
import org.junit.jupiter.api.Test

internal class StreamOfflineMapperTest {
    private val streamOfflineMapper = StreamOfflineMapper()

    @Test
    internal fun `map to incomingEvent`() {
        "/eventsub/payload/stream.offline.json"
            .shouldBeMappedTo(
                streamOfflineMapper,
                StreamOfflineCondition::class,
                StreamOffline(
                    TwitchChannel("Cool_User")
                )
            )
    }
}