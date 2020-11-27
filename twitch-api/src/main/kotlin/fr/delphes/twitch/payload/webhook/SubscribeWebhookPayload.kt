@file:UseSerializers(DurationToSecondSerializer::class)

package fr.delphes.twitch.payload.webhook

import fr.delphes.twitch.serialization.DurationToSecondSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration

@Serializable
data class SubscribeWebhookPayload(
    @SerialName("hub.callback")
    val callback: String,
    @SerialName("hub.mode")
    val mode: SubscribeWebhookMode,
    @SerialName("hub.topic")
    val topic: String,
    @SerialName("hub.lease_seconds")
    val lease_seconds: Duration,
    @SerialName("hub.secret")
    val secret: String? = null
)