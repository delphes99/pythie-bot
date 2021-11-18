package fr.delphes.twitch.api.video.payload

import fr.delphes.utils.serialization.Serializer
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Test

internal class ChannelVideosPayloadTest {
    @Test
    internal fun name() {
        val payload = """
            {
              "_total": 583,
              "videos": [
                {
                  "_id": "v102381501",
                  "broadcast_id": 23711574096,
                  "broadcast_type": "highlight",
                  "channel": {
                    "_id": "20694610",
                    "display_name": "Towelliee",
                    "name": "towelliee"
                  },
                  "created_at": "2016-11-20T23:46:06Z",
                  "description": "Last minutes of stream",
                  "description_html": "Last minutes of stream<br>",
                  "fps": {
                    "chunked": 59.9997939597903,
                    "high": 30.2491085172346,
                    "low": 30.249192959941,
                    "medium": 30.2491085172346,
                    "mobile": 30.249192959941
                  },
                  "game": "World of Warcraft",
                  "language": "en",
                  "length": 201,
                  "preview": {
                    "large": "https://static-cdn.jtvnw.net/s3_vods/664fa5856b_towelliee_23711574096_550644271//thumb/thumb102381501-640x360.jpg",
                    "medium": "https://static-cdn.jtvnw.net/s3_vods/664fa5856b_towelliee_23711574096_550644271//thumb/thumb102381501-320x180.jpg",
                    "small": "https://static-cdn.jtvnw.net/s3_vods/664fa5856b_towelliee_23711574096_550644271//thumb/thumb102381501-80x45.jpg",
                    "template": "https://static-cdn.jtvnw.net/s3_vods/664fa5856b_towelliee_23711574096_550644271//thumb/thumb102381501-{width}x{height}.jpg"
                  },
                  "published_at": "2016-11-20T23:46:06Z",
                  "resolutions": {
                    "chunked": "1920x1080",
                    "high": "1280x720",
                    "low": "640x360",
                    "medium": "852x480",
                    "mobile": "400x226"
                  },
                  "status": "recorded",
                  "tag_list": "",
                  "thumbnails": {
                    "large": [
                      {
                        "type": "generated",
                        "url": "https://static-cdn.jtvnw.net/s3_vods/664fa5856b_towelliee_23711574096_550644271//thumb/thumb102381501-640x360.jpg"
                      }
                    ],
                    "medium": [
                      {
                        "type": "generated",
                        "url": "https://static-cdn.jtvnw.net/s3_vods/664fa5856b_towelliee_23711574096_550644271//thumb/thumb102381501-320x180.jpg"
                      }
                    ],
                    "small": [
                      {
                        "type": "generated",
                        "url": "https://static-cdn.jtvnw.net/s3_vods/664fa5856b_towelliee_23711574096_550644271//thumb/thumb102381501-80x45.jpg"
                      }
                    ],
                    "template": [
                      {
                        "type": "generated",
                        "url": "https://static-cdn.jtvnw.net/s3_vods/664fa5856b_towelliee_23711574096_550644271//thumb/thumb102381501-{width}x{height}.jpg"
                      }
                    ]
                  },
                  "title": "Last minutes of stream",
                  "url": "https://www.twitch.tv/towelliee/v/102381501",
                  "viewable": "public",
                  "viewable_at": null,
                  "views": 1761
                }
              ]
            }
        """.trimIndent()

        val videos = Serializer.decodeFromString<ChannelVideosPayload>(payload)
        videos shouldBe ChannelVideosPayload(
            listOf(
                ChannelVideoPayload(
                    "Last minutes of stream",
                    "World of Warcraft",
                    "https://www.twitch.tv/towelliee/v/102381501"
                )
            )
        )
    }
}