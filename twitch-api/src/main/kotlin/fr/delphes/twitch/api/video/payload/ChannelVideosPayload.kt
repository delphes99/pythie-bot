package fr.delphes.twitch.api.video.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelVideosPayload(
    val data: List<ChannelVideoPayload>
)
