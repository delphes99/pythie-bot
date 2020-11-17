package fr.delphes.twitch.payload.pubsub

import fr.delphes.twitch.payload.pubsub.rewardRedeemed.RewardRedeemedPayload
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class MessagePayload

@Serializable
@SerialName("reward-redeemed")
data class RewardRedeemed(
    val data: RewardRedeemedPayload
): MessagePayload()