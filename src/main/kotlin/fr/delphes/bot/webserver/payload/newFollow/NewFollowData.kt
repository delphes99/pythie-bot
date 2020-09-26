@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.bot.webserver.payload.newFollow

import fr.delphes.storage.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class NewFollowData(
    val from_id: String,
    val from_name: String,
    val to_id: String,
    val to_name: String,
    val followed_at: LocalDateTime
)