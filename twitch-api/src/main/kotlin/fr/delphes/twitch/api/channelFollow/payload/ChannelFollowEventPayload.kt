package fr.delphes.twitch.api.channelFollow.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelFollowEventPayload(
    val user_id: String,
    val user_name: String,
    val broadcaster_user_id: String,
    val broadcaster_user_name: String
)