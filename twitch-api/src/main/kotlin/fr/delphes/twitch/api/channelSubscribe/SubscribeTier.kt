package fr.delphes.twitch.api.channelSubscribe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SubscribeTier {
    @SerialName("1000")
    TIER_1,
    @SerialName("2000")
    TIER_2,
    @SerialName("3000")
    TIER_3,
}