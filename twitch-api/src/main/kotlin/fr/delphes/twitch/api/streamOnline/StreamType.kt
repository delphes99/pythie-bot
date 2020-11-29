package fr.delphes.twitch.api.streamOnline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StreamType {
    @SerialName("live")
    LIVE,
    @SerialName("playlist")
    PLAYLIST,
    @SerialName("watch_party")
    WATCH_PARTY,
    @SerialName("premiere")
    PREMIERE,
    @SerialName("rerun")
    RERUN
}