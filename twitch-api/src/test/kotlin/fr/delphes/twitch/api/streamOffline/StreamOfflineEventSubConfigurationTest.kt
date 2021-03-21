package fr.delphes.twitch.api.streamOffline

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.parseToModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StreamOfflineEventSubConfigurationTest {
    @Test
    fun `transform StreamOffline`() {
        val payload = """
            {
                "subscription": {
                    "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
                    "status": "enabled",
                    "type": "stream.offline",
                    "version": "1",
                    "condition": {
                        "broadcaster_user_id": "1337"
                    },
                    "created_at": "2019-11-16T10:11:12.123Z",
                     "transport": {
                        "method": "webhook",
                        "callback": "https://example.com/webhooks/callback"
                    }
                },
                "event": {
                    "broadcaster_user_id": "1337",
                    "broadcaster_user_name": "cool_user"
                }
            }
        """.trimIndent()

        val model = StreamOfflineEventSubConfiguration().parseToModel(payload, TwitchChannel("channel"))

        assertThat(model).isEqualTo(
            StreamOffline(TwitchChannel("channel"))
        )
    }
}