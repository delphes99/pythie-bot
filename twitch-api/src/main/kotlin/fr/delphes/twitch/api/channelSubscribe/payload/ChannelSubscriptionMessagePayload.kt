package fr.delphes.twitch.api.channelSubscribe.payload

import fr.delphes.twitch.api.payload.MessagePayload
import kotlinx.serialization.Serializable

@Serializable
data class ChannelSubscriptionMessagePayload(
    val broadcaster_user_id: String,
    val broadcaster_user_login: String,
    val broadcaster_user_name: String,
    val cumulative_months: Int,
    val duration_months: Int,
    val message: MessagePayload,
    val streak_months: Int,
    val tier: String,
    val user_id: String,
    val user_login: String,
    val user_name: String
)

