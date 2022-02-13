package fr.delphes.twitch.api.video.payload

import fr.delphes.utils.serialization.Serializer
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class ChannelVideosPayloadTest {
    @Test
    internal fun deserialize() {
        val payload = """
            {
              "data": [
                {
                  "id": "335921245",
                  "stream_id": null,
                  "user_id": "141981764",
                  "user_login": "twitchdev",
                  "user_name": "TwitchDev",
                  "title": "Twitch Developers 101",
                  "description": "Welcome to Twitch development! Here is a quick overview of our products and information to help you get started.",
                  "created_at": "2018-11-14T21:30:18Z",
                  "published_at": "2018-11-14T22:04:30Z",
                  "url": "https://www.twitch.tv/videos/335921245",
                  "thumbnail_url": "https://static-cdn.jtvnw.net/cf_vods/d2nvs31859zcd8/twitchdev/335921245/ce0f3a7f-57a3-4152-bc06-0c6610189fb3/thumb/index-0000000000-%{width}x%{height}.jpg",
                  "viewable": "public",
                  "view_count": 1863062,
                  "language": "en",
                  "type": "upload",
                  "duration": "3m21s",
                  "muted_segments": [
                    {
                      "duration": 30,
                      "offset": 120
                    }
                  ]
                }
              ],
              "pagination": {}
            }
        """.trimIndent()

        val videos = Serializer.decodeFromString<ChannelVideosPayload>(payload)
        videos shouldBe ChannelVideosPayload(
            listOf(
                ChannelVideoPayload(
                    title = "Twitch Developers 101",
                    url = "https://www.twitch.tv/videos/335921245",
                    created_at = LocalDateTime.of(2018, 11, 14, 21, 30, 18),
                    type = ChannelVideoType.upload,
                    description = "Welcome to Twitch development! Here is a quick overview of our products and information to help you get started."
                )
            )
        )
    }
}