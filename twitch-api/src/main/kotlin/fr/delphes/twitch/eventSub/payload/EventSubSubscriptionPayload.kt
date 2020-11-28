@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.eventSub.payload

import fr.delphes.twitch.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class EventSubSubscriptionPayload(
    val id: String,
    val status: EventSubSubscriptionStatus,
    val type: EventSubSubscriptionType,
    val version: String,
    val created_at: LocalDateTime
)