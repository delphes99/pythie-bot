package fr.delphes.twitch.api.reward.payload.getCustomReward

import kotlinx.serialization.Serializable

@Serializable
data class ImagePayload(
    val url_1x: String,
    val url_2x: String,
    val url_4x: String
)