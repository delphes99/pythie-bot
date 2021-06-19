package fr.delphes.twitch.api.channelPrediction.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelPredictionEnd(
    override val condition: ChannelPredictionEndCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<ChannelPredictionEndCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_PREDICTION_END
}