package fr.delphes.twitch.api.channelCheer.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelCheerEventPayload(
    val is_anonymous: Boolean,
    val user_id: String? = null,
    val user_name: String? = null,
    val broadcaster_user_id: String,
    val broadcaster_user_name: String,
    val message: String? = null,
    val bits: Long
)