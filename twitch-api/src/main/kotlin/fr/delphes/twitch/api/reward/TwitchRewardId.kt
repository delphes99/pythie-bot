package fr.delphes.twitch.api.reward

import kotlinx.serialization.Serializable

//TODO delete ?
@Serializable
data class TwitchRewardId(
    val id: String,
    val name: String,
)