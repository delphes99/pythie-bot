package fr.delphes.twitch.api.channelSubscribe.payload

import fr.delphes.twitch.api.channelSubscribe.SubscribeTier
import kotlinx.serialization.Serializable

@Serializable
data class ChannelSubscribeEventPayload(
    val user_id: String,
    val user_name: String,
    val broadcaster_user_id: String,
    val broadcaster_user_name: String,
    val tier: SubscribeTier,
    val is_gift: Boolean
)