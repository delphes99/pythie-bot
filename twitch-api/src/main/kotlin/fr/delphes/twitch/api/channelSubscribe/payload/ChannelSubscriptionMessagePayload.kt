package fr.delphes.twitch.api.channelSubscribe.payload

import fr.delphes.twitch.api.channelSubscribe.SubscribeTier
import fr.delphes.twitch.api.payload.MessagePayload
import kotlinx.serialization.Serializable

@Serializable
data class ChannelSubscriptionMessagePayload(
    val user_id: String,
    val user_name: String,
    val tier: SubscribeTier,
    val message: MessagePayload,
    val cumulative_months: Int,
    val streak_months: Int,
    val duration_months: Int,
)
