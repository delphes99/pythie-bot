package fr.delphes.twitch.api.channelPointsCustomRewardRedemption

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RewardRedemptionStatus {
    @SerialName("unknown")
    UNKNOWN,
    @SerialName("unfulfilled")
    UNFULFILLED,
    @SerialName("fulfilled")
    FULFILLED,
    @SerialName("canceled")
    CANCELED
}
