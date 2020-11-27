@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.webhook

import fr.delphes.twitch.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class GetWebhookSubscriptionsDataPayload(
    val topic: String,
    val callback: String,
    val expires_at: LocalDateTime
)