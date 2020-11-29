package fr.delphes.twitch.api.streamOnline

import fr.delphes.twitch.api.parseToModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class StreamOnlineEventSubConfigurationTest {
    @Test
    fun `transform StreamOnline`() {
        val payload = """
            {
                "subscription": {
                    "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
                    "status": "enabled",
                    "type": "stream.online",
                    "version": "1",
                    "condition": {
                        "broadcaster_user_id": "1337"
                    },
                     "transport": {
                        "method": "webhook",
                        "callback": "https://example.com/webhooks/callback"
                    },
                    "created_at": "2019-11-16T10:11:12.123Z"
                },
                "event": {
                    "id": "9001",
                    "broadcaster_user_id": "1337",
                    "broadcaster_user_name": "cool_user",
                    "type": "live"
                }
            }
        """.trimIndent()

        val model = StreamOnlineEventSubConfiguration { }.parseToModel(payload)

        Assertions.assertThat(model).isEqualTo(
            StreamOnline(StreamType.LIVE)
        )
    }
}