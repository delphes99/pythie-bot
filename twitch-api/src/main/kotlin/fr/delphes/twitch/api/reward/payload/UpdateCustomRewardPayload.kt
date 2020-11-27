package fr.delphes.twitch.api.reward.payload

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCustomRewardPayload(
    val is_enabled: Boolean
)