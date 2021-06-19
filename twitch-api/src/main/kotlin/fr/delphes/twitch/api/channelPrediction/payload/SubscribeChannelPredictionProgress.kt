package fr.delphes.twitch.api.channelPrediction.payload

import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowCondition
import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelPredictionProgress(
    override val condition: ChannelPredictionProgressCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<ChannelPredictionProgressCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_PREDICTION_PROGRESS
}