package fr.delphes.twitch.api.channelSubscribe.payload

import fr.delphes.twitch.api.channelSubscribe.SubscribeTier.TIER_1
import fr.delphes.twitch.api.payload.MessageEmotePayload
import fr.delphes.twitch.api.payload.MessagePayload
import fr.delphes.utils.serialization.Serializer
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString

class ChannelSubscriptionMessagePayloadTest : ShouldSpec({
    should("deserialize") {
        Serializer.decodeFromString<ChannelSubscriptionMessagePayload>(
            """
            {
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
        """
        ).shouldBe(
            ChannelSubscriptionMessagePayload(
                broadcaster_user_id = "1337",
                broadcaster_user_login = "cooler_user",
                broadcaster_user_name = "Cooler_User",
                user_id = "1234",
                user_login = "cool_user",
                user_name = "Cool_User",
                tier = TIER_1,
                message = MessagePayload(
                    text = "Love the stream! FevziGG",
                    emotes = listOf(
                        MessageEmotePayload(
                            begin = 23,
                            end = 30,
                            id = "302976485"
                        )
                    )
                ),
                cumulative_months = 15,
                streak_months = 1,
                duration_months = 6
            )
        )
    }
})