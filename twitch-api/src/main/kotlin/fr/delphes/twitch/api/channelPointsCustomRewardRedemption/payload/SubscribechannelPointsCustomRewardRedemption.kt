package fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribechannelPointsCustomRewardRedemption(
    override val condition: ChannelPointsCustomRewardRedemptionCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<ChannelPointsCustomRewardRedemptionCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION
}