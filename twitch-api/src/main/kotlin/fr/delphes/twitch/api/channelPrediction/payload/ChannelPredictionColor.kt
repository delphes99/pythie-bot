package fr.delphes.twitch.api.channelPrediction.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ChannelPredictionColor {
    @SerialName("blue")
    BLUE,
    @SerialName("pink")
    PINK,
}
