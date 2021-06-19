package fr.delphes.twitch.api.channelPrediction.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelPredictionChoicePayload(
    val id: String,
    val title: String,
    val color: ChannelPredictionColor,
    val users: Int = 0,
    val channel_points: Int = 0,
    val top_predictors: List<ChannelPredictionPredictorPayload> = emptyList()
)
