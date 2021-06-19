package fr.delphes.twitch.api.channelPrediction.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ChannelPredictionStatus {
    @SerialName("resolved")
    RESOLVED,
    @SerialName("canceled")
    CANCELED,
}
