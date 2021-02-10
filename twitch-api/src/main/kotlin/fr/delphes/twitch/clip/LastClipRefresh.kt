@file:UseSerializers(LocalDateTimeSerializer::class)

package fr.delphes.twitch.clip

import fr.delphes.utils.serialization.LocalDateTimeSerializer
import kotlinx.serialization.UseSerializers

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class LastClipRefresh(
    val last_id_notified: String,
    val last_start_date_checked: LocalDateTime
)