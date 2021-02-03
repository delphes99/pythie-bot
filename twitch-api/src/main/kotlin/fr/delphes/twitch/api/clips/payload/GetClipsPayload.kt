@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.api.clips.payload

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
)