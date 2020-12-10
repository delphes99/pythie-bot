package fr.delphes.twitch.api.reward.payload.getCustomReward

import kotlinx.serialization.Serializable

@Serializable
data class MaxPerUserPerStreamPayload(
    val is_enabled: Boolean,
    val max_per_user_per_stream: Long
)