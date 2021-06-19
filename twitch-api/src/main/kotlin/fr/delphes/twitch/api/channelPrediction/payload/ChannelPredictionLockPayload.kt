@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.api.channelPrediction.payload

import fr.delphes.utils.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class ChannelPredictionLockPayload(
    val title: String,
    val outcomes: List<ChannelPredictionChoicePayload>,
    val started_at: LocalDateTime,
    val locked_at: LocalDateTime
)
