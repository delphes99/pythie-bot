package fr.delphes.twitch.api.channelUpdate.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelUpdateEventPayload(
    val broadcaster_user_id: String,
    val broadcaster_user_name: String,
    val title: String,
    val language: String,
    val category_id: String,
    val category_name: String,
    val is_mature: Boolean
)