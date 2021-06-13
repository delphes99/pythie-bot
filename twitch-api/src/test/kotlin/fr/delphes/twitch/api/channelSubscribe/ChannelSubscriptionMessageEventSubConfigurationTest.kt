package fr.delphes.twitch.api.channelSubscribe

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.parseToModel
import fr.delphes.twitch.api.user.User
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ChannelSubscriptionMessageEventSubConfigurationTest {
    @Test
    fun `transform NewSub`() {
        val payload = """
            {
                "subscription": {
                    "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
                    "type": "channel.subscription.message",
                    "version": "beta",
                    "status": "enabled",
                    "cost": 0,
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
                    "user_login": "cool_user",
                    "user_name": "Cool_User",
                    "broadcaster_user_id": "1337",
                    "broadcaster_user_login": "cooler_user",
                    "broadcaster_user_name": "Cooler_User",
                    "tier": "1000",
                    "message": {
                        "text": "Love the stream! FevziGG",
                        "emotes": [
                            {
                                "begin": 23,
                                "end": 30,
                                "id": "302976485"
                            }
                        ]
                    },
                    "cumulative_months": 15,
                    "streak_months": 1,
                    "duration_months": 6
                }
            }
        """

        val model = ChannelSubscriptionMessageEventSubConfiguration().parseToModel(payload, TwitchChannel("channel"))

        model.shouldBe(
            NewSub(
                TwitchChannel("channel"),
                User("Cool_User"),
                SubscribeTier.TIER_1,
                false
            )
        )
    }
}