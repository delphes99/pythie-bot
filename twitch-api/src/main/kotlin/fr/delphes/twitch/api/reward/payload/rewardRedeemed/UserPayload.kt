package fr.delphes.twitch.api.reward.payload.rewardRedeemed

import kotlinx.serialization.Serializable

@Serializable
data class UserPayload(
    val id: String,
    val login: String,
    val display_name: String
)