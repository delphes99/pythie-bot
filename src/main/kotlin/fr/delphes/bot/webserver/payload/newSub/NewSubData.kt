@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.bot.webserver.payload.newSub

import fr.delphes.bot.storage.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class NewSubData(
    val id: String,
    val event_type: String,
    val event_timestamp: LocalDateTime,
    val version: Double,
    val event_data: NewSubEventData
)