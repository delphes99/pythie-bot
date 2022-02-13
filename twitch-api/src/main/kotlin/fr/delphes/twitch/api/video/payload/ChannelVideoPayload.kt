@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.api.video.payload

import fr.delphes.utils.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class ChannelVideoPayload(
    val title: String,
    val url: String,
    val created_at: LocalDateTime,
    val type: ChannelVideoType,
    val description: String? = null,
)