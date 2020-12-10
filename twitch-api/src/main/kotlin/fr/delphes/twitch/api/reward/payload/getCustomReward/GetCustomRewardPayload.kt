package fr.delphes.twitch.api.reward.payload.getCustomReward

import kotlinx.serialization.Serializable

@Serializable
data class GetCustomRewardPayload(
    val data: List<GetCustomRewardDataPayload>
)