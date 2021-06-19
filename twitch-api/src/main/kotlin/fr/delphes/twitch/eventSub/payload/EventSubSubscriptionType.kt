package fr.delphes.twitch.eventSub.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventSubSubscriptionType {
    @SerialName("channel.follow")
    CHANNEL_FOLLOW,
    @SerialName("channel.subscribe")
    CHANNEL_SUBSCRIBE,
    @SerialName("channel.cheer")
    CHANNEL_CHEER,
    @SerialName("channel.channel_points_custom_reward_redemption.add")
    CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION,
    @SerialName("channel.update")
    CHANNEL_UPDATE,
    @SerialName("stream.offline")
    STREAM_OFFLINE,
    @SerialName("stream.online")
    STREAM_ONLINE,
    @SerialName("channel.subscription.message")
    CHANNEL_SUBSCRIPTION_MESSAGE,
    @SerialName("channel.raid")
    CHANNEL_RAID,
    @SerialName("channel.poll.begin")
    CHANNEL_POLL_BEGIN,
    @SerialName("channel.poll.progress")
    CHANNEL_POLL_PROGRESS,
    @SerialName("channel.poll.end")
    CHANNEL_POLL_END,
    @SerialName("channel.prediction.begin")
    CHANNEL_PREDICTION_BEGIN,
    @SerialName("channel.prediction.progress")
    CHANNEL_PREDICTION_PROGRESS,
    @SerialName("channel.prediction.lock")
    CHANNEL_PREDICTION_LOCK,
    @SerialName("channel.prediction.end")
    CHANNEL_PREDICTION_END;
}