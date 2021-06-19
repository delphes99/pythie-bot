@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.utils.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class ChannelPollProgressPayload(
    val id: String,
    val title: String,
    val choices: List<ChannelPollChoiceResult>,
    val bits_voting: ChannelPollPointsConfiguration,
    val channel_points_voting: ChannelPollPointsConfiguration,
    val started_at: LocalDateTime,
    val ends_at: LocalDateTime
)