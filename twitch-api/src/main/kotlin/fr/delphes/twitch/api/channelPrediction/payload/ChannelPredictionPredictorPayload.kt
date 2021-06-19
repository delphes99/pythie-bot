package fr.delphes.twitch.api.channelPrediction.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelPredictionPredictorPayload(
    val user_name: String,
    val user_login: String,
    val user_id: String,
    val channel_points_won: Int? = null,
    val channel_points_used: Int
)
