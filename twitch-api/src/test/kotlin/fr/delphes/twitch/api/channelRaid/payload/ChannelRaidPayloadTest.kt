package fr.delphes.twitch.api.channelRaid.payload

import fr.delphes.utils.serialization.Serializer
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Test

internal class ChannelRaidPayloadTest {
    @Test
    internal fun deserialize() {
        Serializer.decodeFromString<ChannelRaidPayload>(
            """
                {
                    "from_broadcaster_user_id": "1234",
                    "from_broadcaster_user_login": "cool_user",
                    "from_broadcaster_user_name": "Cool_User",
                    "to_broadcaster_user_id": "1337",
                    "to_broadcaster_user_login": "cooler_user",
                    "to_broadcaster_user_name": "Cooler_User",
                    "viewers": 9001
                }
            """
        ).shouldBe(
            ChannelRaidPayload(
                from_broadcaster_user_id = "1234",
                from_broadcaster_user_login = "cool_user",
                from_broadcaster_user_name = "Cool_User",
                to_broadcaster_user_id = "1337",
                to_broadcaster_user_login = "cooler_user",
                to_broadcaster_user_name = "Cooler_User",
                viewers = 9001
            )
        )
    }
}