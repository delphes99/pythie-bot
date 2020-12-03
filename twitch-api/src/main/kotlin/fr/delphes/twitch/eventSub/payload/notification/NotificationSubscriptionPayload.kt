@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.eventSub.payload.notification

import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionStatus
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.utils.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class NotificationSubscriptionPayload<T>(
    val id: String,
    val type: EventSubSubscriptionType,
    val version: String,
    val status: EventSubSubscriptionStatus,
    val created_at: LocalDateTime,
    val condition: T
)