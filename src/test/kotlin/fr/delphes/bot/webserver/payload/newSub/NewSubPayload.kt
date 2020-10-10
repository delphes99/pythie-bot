package fr.delphes.bot.webserver.payload.newSub

import fr.delphes.bot.storage.serialization.Serializer
import org.junit.jupiter.api.Test
import kotlinx.serialization.decodeFromString
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime

internal class NewSubPayloadTest {
    @Test
    internal fun deserialize() {
        assertThat(
            Serializer.decodeFromString<NewSubPayload>(
                """
                    {
                      "data": [
                        {
                          "id": "3Wba7BrK0NQuEX9BO8emK8aHfpK",
                          "event_type": "subscriptions.subscribe",
                          "event_timestamp": "2020-03-16T16:31:42Z",
                          "version": "1.0",
                          "event_data": {
                            "broadcaster_id": "158038007",
                            "broadcaster_name": "meka",
                            "is_gift": true,
                            "plan_name": "Channel Subscription (meka)",
                            "tier": "1000",
                            "user_id": "505037911",
                            "user_name": "hami0315",
                            "gifter_id": "156900877",
                            "gifter_name": "baxter4343"
                          }
                        }
                      ]
                    }
                """.trimIndent()
            )
        ).isEqualTo(
            NewSubPayload(
                NewSubData(
                    "3Wba7BrK0NQuEX9BO8emK8aHfpK",
                    "subscriptions.subscribe",
                    LocalDateTime.of(2020, 3, 16, 16, 31, 42),
                    1.0,
                    NewSubEventData(
                        "158038007",
                        "meka",
                        true,
                        "Channel Subscription (meka)",
                        "1000",
                        "505037911",
                        "hami0315",
                        "156900877",
                        "baxter4343"
                    )
                )
            )
        )
    }
}