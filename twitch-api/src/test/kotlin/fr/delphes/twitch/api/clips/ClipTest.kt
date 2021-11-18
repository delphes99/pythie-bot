package fr.delphes.twitch.api.clips

import fr.delphes.twitch.api.clips.payload.GetClips
import fr.delphes.twitch.api.clips.payload.GetClipsPayload
import fr.delphes.utils.serialization.Serializer
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class ClipTest {
    @Test
    fun `deserialize GetStream with empty tags`() {
        val payloadStr = """
            {
              "data": [
                {
                  "id":"RandomClip1",
                  "url":"https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage",
                  "embed_url":"https://clips.twitch.tv/embed?clip=RandomClip1",
                  "broadcaster_id":"1234",
                  "broadcaster_name": "JJ",
                  "creator_id":"123456",
                  "creator_name": "MrMarshall",
                  "video_id":"1234567",
                  "game_id":"33103",
                  "language":"en",
                  "title":"random1",
                  "view_count":10,
                  "created_at":"2017-11-30T22:34:18Z",
                  "thumbnail_url":"https://clips-media-assets.twitch.tv/157589949-preview-480x272.jpg"
                }
              ],
              "pagination": {
                "cursor": "eyJiIjpudWxsLCJhIjoiIn0"
              }
            }
        """.trimIndent()

        val model = Serializer.decodeFromString<GetClips>(payloadStr)

        model shouldBe GetClips(
            listOf(
                GetClipsPayload(
                    "RandomClip1",
                    "https://clips.twitch.tv/AwkwardHelplessSalamanderSwiftRage",
                    "https://clips.twitch.tv/embed?clip=RandomClip1",
                    "1234",
                    "JJ",
                    "123456",
                    "MrMarshall",
                    "1234567",
                    "33103",
                    "en",
                    "random1",
                    10,
                    LocalDateTime.of(2017, 11, 30, 22, 34, 18),
                    "https://clips-media-assets.twitch.tv/157589949-preview-480x272.jpg"
                )
            )
        )
    }
}