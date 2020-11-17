@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.payload.streams

import fr.delphes.twitch.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class StreamInfos(
    val id: String,
    val user_id: String,
    val user_name: String,
    val game_id: String,
    val type: String,
    val title: String,
    val viewer_count: Long,
    val started_at: LocalDateTime,
    val language: String,
    val thumbnail_url: String,
    val tag_ids: List<String>
)