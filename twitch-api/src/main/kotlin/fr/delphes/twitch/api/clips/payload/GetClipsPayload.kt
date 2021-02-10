@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.api.clips.payload

import fr.delphes.twitch.api.clips.Clip
import fr.delphes.twitch.api.streams.ThumbnailUrl
import fr.delphes.twitch.api.user.TwitchUser
import fr.delphes.utils.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class GetClipsPayload(
    val id: String,
    val url: String,
    val embed_url: String,
    val broadcaster_id: String,
    val broadcaster_name: String,
    val creator_id: String,
    val creator_name: String,
    val video_id: String,
    val game_id: String,
    val language: String,
    val title: String,
    val view_count: Long,
    val created_at: LocalDateTime,
    val thumbnail_url: String
) {
    fun toClip(): Clip {
        return Clip(
            url,
            TwitchUser(creator_id, creator_name),
            game_id, //TODO game repository to retrieve game name
            title,
            created_at,
            ThumbnailUrl(thumbnail_url)
        )
    }
}