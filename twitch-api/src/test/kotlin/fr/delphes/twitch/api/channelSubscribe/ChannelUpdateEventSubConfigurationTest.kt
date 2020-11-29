package fr.delphes.twitch.api.channelSubscribe

import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.channelUpdate.ChannelUpdateEventSubConfiguration
import fr.delphes.twitch.api.parseToModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ChannelUpdateEventSubConfigurationTest {
    @Test
    fun `transform ChannelUpdate`() {
        val payload = """
            {
                "subscription": {
                    "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
                    "status": "enabled",
                    "type": "channel.update",
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
                    "broadcaster_user_id": "1337",
                    "broadcaster_user_name": "cool_user",
                    "title": "Best Stream Ever",
                    "language": "en",
                    "category_id": "21779",
                    "category_name": "Fortnite",
                    "is_mature": false
                }
            }
        """.trimIndent()

        val model = ChannelUpdateEventSubConfiguration { }.parseToModel(payload)

        assertThat(model).isEqualTo(
            ChannelUpdate("Best Stream Ever", "en", "21779", "Fortnite")
        )
    }
}