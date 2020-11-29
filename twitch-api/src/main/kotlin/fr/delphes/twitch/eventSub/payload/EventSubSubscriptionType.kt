package fr.delphes.twitch.eventSub.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventSubSubscriptionType {
    @SerialName("channel.follow")
    CHANNEL_FOLLOW,
    @SerialName("channel.subscribe")
    CHANNEL_SUBSCRIBE,
    @SerialName("channel.update")
    CHANNEL_UPDATE,
    @SerialName("stream.offline")
    STREAM_OFFLINE,
    @SerialName("stream.online")
    STREAM_ONLINE;
}