package fr.delphes.twitch.eventSub

enum class EventSubTopic(
    val webhookPathSuffix: String
) {
    CHANNEL_UPDATE("channelUpdate"),
    CUSTOM_REWARD_REDEMPTION("customRewardRedemption"),
    NEW_CHEER("newCheer"),
    NEW_FOLLOW("newFollow"),
    NEW_SUB("newSub"),
    STREAM_OFFLINE("streamOffline"),
    STREAM_ONLINE("streamOnline");
}