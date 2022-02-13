package fr.delphes.twitch.api.video.payload

import kotlinx.serialization.Serializable

@Serializable
enum class ChannelVideoType {
    all,
    upload,
    archive,
    highlight,
}