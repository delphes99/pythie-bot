package fr.delphes.twitch.api.video.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelVideoPayload(
    val title: String,
    val game: String,
    val url: String,
)
