@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.utils.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class ChannelPollEndPayload(
    val id: String,
    val broadcaster_user_id: String,
    val broadcaster_user_login: String,
    val broadcaster_user_name: String,
    val title: String,
    val choices: List<ChannelPollChoiceResult>,
    val bits_voting: ChannelPollPointsConfiguration,
    val channel_points_voting: ChannelPollPointsConfiguration,
    val started_at: LocalDateTime,
    val ended_at: LocalDateTime
)
