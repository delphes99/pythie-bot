package fr.delphes.twitch.api.channel.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelInformation(
    val broadcaster_id: String,
    val broadcaster_login: String,
    val broadcaster_name: String,
    val broadcaster_language: String,
    val game_id: String,
    val game_name: String,
    val title: String,
    val delay: Int
)
