package fr.delphes.twitch.payload.reward

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCustomRewardPayload(
    val is_enabled: Boolean
)