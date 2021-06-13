package fr.delphes.twitch.api.channelRaid

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.parseToModel
import fr.delphes.twitch.api.user.User
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ChannelRaidEventSubConfigurationTest {
    @Test
    fun `transform IncomingRaid`() {
        val payload = """
            {
                "subscription": {
                    "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
                    "type": "channel.raid",
                    "version": "1",
                    "status": "enabled",
                    "cost": 0,
                    "condition": {
                        "to_broadcaster_user_id": "1337"
                    },
                     "transport": {
                        "method": "webhook",
                        "callback": "https://example.com/webhooks/callback"
                    },
                    "created_at": "2019-11-16T10:11:12.123Z"
                },
                "event": {
                    "from_broadcaster_user_id": "1234",
                    "from_broadcaster_user_login": "cool_user",
                    "from_broadcaster_user_name": "Cool_User",
                    "to_broadcaster_user_id": "1337",
                    "to_broadcaster_user_login": "cooler_user",
                    "to_broadcaster_user_name": "Cooler_User",
                    "viewers": 9001
                }
            }
        """

        val model = ChannelRaidEventSubConfiguration().parseToModel(payload, TwitchChannel("channel"))

        model.shouldBe(
            IncomingRaid(
                TwitchChannel("channel"),
                User("Cool_User"),
                9001
            )
        )
    }
}