@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.bot.webserver.payload.streamInfos

import fr.delphes.bot.util.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class StreamInfosData(
    val id: String,
    val user_id: String,
    val user_name: String,
    val game_id: String,
    val community_ids: List<String>? = null,
    val type: String,
    val title: String,
    val viewer_count: Long,
    val started_at: LocalDateTime,
    val language: String,
    val thumbnail_url: String
)