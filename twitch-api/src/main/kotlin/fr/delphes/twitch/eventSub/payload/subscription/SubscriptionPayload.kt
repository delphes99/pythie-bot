@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.eventSub.payload.subscription

import fr.delphes.twitch.eventSub.payload.Transport
import fr.delphes.utils.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class SubscriptionPayload(
    val id: String,
    val status: String,
    val type: String,
    val version: String,
    val created_at: LocalDateTime,
    val transport: Transport
)