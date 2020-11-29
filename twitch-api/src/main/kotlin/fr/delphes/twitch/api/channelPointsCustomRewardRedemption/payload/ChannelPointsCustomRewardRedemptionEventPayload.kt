@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemptionStatus
import fr.delphes.twitch.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class ChannelPointsCustomRewardRedemptionEventPayload(
    val id: String,
    val broadcaster_user_id: String,
    val broadcaster_user_name: String,
    val user_id: String,
    val user_name: String,
    val user_input: String,
    val status: RewardRedemptionStatus,
    val reward: ChannelPointsCustomRewardRedemptionRewardPayload,
    val redeemed_at: LocalDateTime
)