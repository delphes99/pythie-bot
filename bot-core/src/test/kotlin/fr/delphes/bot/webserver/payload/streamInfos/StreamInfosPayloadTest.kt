package fr.delphes.bot.webserver.payload.streamInfos

import fr.delphes.bot.util.serialization.Serializer
import kotlinx.serialization.decodeFromString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class StreamInfosPayloadTest {
    @Test
    internal fun `deserialize stream off`() {
        assertThat(
            Serializer.decodeFromString<StreamInfosPayload>(
                """
                    {
                      "data": []
                    }
                """.trimIndent()
            )
        ).isEqualTo(
            StreamInfosPayload()
        )
    }

    @Test
    internal fun `deserialize stream on`() {
        assertThat(
            Serializer.decodeFromString<StreamInfosPayload>(
                """
                    {
                      "data": [
                        {
                          "id": "0123456789",
                          "user_id": "5678",
                          "user_name": "wjdtkdqhs",
                          "game_id": "21779",
                          "community_ids": [],
                          "type": "live",
                          "title": "Best Stream Ever",
                          "viewer_count": 417,
                          "started_at": "2017-12-01T10:09:45Z",
                          "language": "en",
                          "thumbnail_url": "https://link/to/thumbnail.jpg"
                        }
                      ]
                    }
                """.trimIndent()
            )
        ).isEqualTo(
            StreamInfosPayload(
                StreamInfosData(
                    "0123456789",
                    "5678",
                    "wjdtkdqhs",
                    "21779",
                    emptyList(),
                    "live",
                    "Best Stream Ever",
                    417,
                    LocalDateTime.of(2017, 12, 1, 10, 9, 45),
                    "en",
                    "https://link/to/thumbnail.jpg"
                )
            )
        )
    }
}