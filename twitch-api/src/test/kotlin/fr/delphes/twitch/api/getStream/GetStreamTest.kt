package fr.delphes.twitch.api.getStream

import fr.delphes.twitch.api.streams.payload.StreamInfos
import fr.delphes.twitch.api.streams.payload.StreamPayload
import fr.delphes.utils.serialization.Serializer
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import java.time.LocalDateTime

class GetStreamTest : ShouldSpec({
    should("deserialize GetStream with empty tags") {
        val payloadStr = """
            {
              "data": [
                {
                  "id": "40321373372",
                  "user_id": "576689294",
                  "user_name": "delphestest",
                  "game_id": "509670",
                  "game_name": "Science \u0026 Technology",
                  "type": "live",
                  "title": "test bbb",
                  "viewer_count": 0,
                  "started_at": "2020-12-12T08:07:42Z",
                  "language": "en",
                  "thumbnail_url": "https://static-cdn.jtvnw.net/previews-ttv/live_user_delphestest-{width}x{height}.jpg",
                  "tag_ids": null
                }
              ],
              "pagination": {}
            }
        """.trimIndent()

        val model = Serializer.decodeFromString<StreamPayload>(payloadStr)

        model shouldBe StreamPayload(
            StreamInfos(
                "40321373372",
                "576689294",
                "delphestest",
                "509670",
                "live",
                "test bbb",
                0,
                LocalDateTime.of(2020, 12, 12, 8, 7, 42),
                "en",
                "https://static-cdn.jtvnw.net/previews-ttv/live_user_delphestest-{width}x{height}.jpg",
                emptyList()
            )
        )
    }
})