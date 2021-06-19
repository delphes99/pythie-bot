@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.api.channelPrediction.payload

import fr.delphes.utils.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class ChannelPredictionEndPayload(
    val title: String,
    val winning_outcome_id: String,
    val outcomes: List<ChannelPredictionChoicePayload>,
    val status: ChannelPredictionStatus,
    val started_at: LocalDateTime,
    val ended_at: LocalDateTime
)
