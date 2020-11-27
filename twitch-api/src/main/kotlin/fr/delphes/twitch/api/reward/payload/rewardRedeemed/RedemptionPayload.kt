@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.api.reward.payload.rewardRedeemed

import fr.delphes.twitch.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class RedemptionPayload(
    val id: String,
    val user: UserPayload,
    val channel_id: String,
    val redeemed_at: LocalDateTime,
    val reward: RewardPayload,
    val status: String,
    val cursor: String
)