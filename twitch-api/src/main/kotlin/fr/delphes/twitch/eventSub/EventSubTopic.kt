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
    STREAM_ONLINE("streamOnline"),
    CHANNEL_SUBSCRIPTION_MESSAGE("subscriptionMessage"),
    INCOMING_RAID("incomingRaid"),
    CHANNEL_POLL_BEGIN("pollBegin"),
    CHANNEL_POLL_PROGRESS("pollProgress"),
    CHANNEL_POLL_END("pollEnd"),
    CHANNEL_PREDICTION_BEGIN("predictionBegin"),
    CHANNEL_PREDICTION_PROGRESS("predictionProgress"),
    CHANNEL_PREDICTION_LOCK("predictionLock"),
    CHANNEL_PREDICTION_END("predictionEnd");
}