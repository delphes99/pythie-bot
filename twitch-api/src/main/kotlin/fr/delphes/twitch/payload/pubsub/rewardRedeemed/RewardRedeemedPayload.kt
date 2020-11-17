@file:UseSerializers(LocalDateTimeAsInstantSerializer::class)

package fr.delphes.twitch.payload.pubsub.rewardRedeemed

import fr.delphes.twitch.serialization.LocalDateTimeAsInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime

@Serializable
data class RewardRedeemedPayload(
    val timestamp: LocalDateTime,
    val redemption: RedemptionPayload
)