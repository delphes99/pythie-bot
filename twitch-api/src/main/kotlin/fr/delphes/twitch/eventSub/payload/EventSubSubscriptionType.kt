package fr.delphes.twitch.eventSub.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventSubSubscriptionType {
    @SerialName("channel.follow")
    CHANNEL_FOLLOW,
    @SerialName("channel.update")
    CHANNEL_UPDATE,
    @SerialName("stream.online")
    STREAM_ONLINE;
}