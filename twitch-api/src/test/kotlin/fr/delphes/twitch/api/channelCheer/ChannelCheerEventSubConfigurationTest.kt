package fr.delphes.twitch.api.channelCheer

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.parseToModel
import fr.delphes.twitch.api.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ChannelCheerEventSubConfigurationTest {
    @Test
    fun `transform NewCheer`() {
        val payload = """
            {
                "subscription": {
                    "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
                    "status": "enabled",
                    "type": "channel.cheer",
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
                    "is_anonymous": false,
                    "user_id": "1234",
                    "user_name": "cool_user",
                    "broadcaster_user_id": "1337",
                    "broadcaster_user_name": "cooler_user",
                    "message": "pogchamp",
                    "bits": 1000
                }
            }
        """.trimIndent()

        val channel = TwitchChannel("channel")
        val model = ChannelCheerEventSubConfiguration().parseToModel(payload, channel)

        assertThat(model).isEqualTo(
            NewCheer(channel, User("cool_user"), 1000, "pogchamp")
        )
    }
}