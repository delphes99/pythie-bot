package fr.delphes.twitch.api.channelRaid.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelRaidPayload(
    val from_broadcaster_user_id: String,
    val from_broadcaster_user_login: String,
    val from_broadcaster_user_name: String,
    val to_broadcaster_user_id: String,
    val to_broadcaster_user_login: String,
    val to_broadcaster_user_name: String,
    val viewers: Long,
)
