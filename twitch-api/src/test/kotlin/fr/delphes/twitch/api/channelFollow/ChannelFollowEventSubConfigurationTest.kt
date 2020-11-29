package fr.delphes.twitch.api.channelFollow

import fr.delphes.twitch.api.parseToModel
import fr.delphes.twitch.api.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ChannelFollowEventSubConfigurationTest {
    @Test
    fun `transform NewFollow`() {
        val payload = """
            {
                "subscription": {
                    "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
                    "status": "enabled",
                    "type": "channel.follow",
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
                    "user_id": "1234",
                    "user_name": "cool_user",
                    "broadcaster_user_id": "1337",
                    "broadcaster_user_name": "cooler_user"
                }
            }
        """.trimIndent()

        val model = ChannelFollowEventSubConfiguration { }.parseToModel(payload)

        assertThat(model).isEqualTo(
            NewFollow(User("cool_user"))
        )
    }
}