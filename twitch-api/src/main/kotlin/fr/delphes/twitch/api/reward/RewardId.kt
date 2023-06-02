package fr.delphes.twitch.api.reward

import kotlinx.serialization.Serializable

@Serializable
data class RewardId(
    val id: String,
    val name: String,
)