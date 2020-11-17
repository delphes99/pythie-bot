package fr.delphes.twitch.payload.pubsub.rewardRedeemed

import kotlinx.serialization.Serializable

@Serializable
data class DefaultImagePayload(
    val url_1x: String,
    val url_2x: String,
    val url_4x: String
)