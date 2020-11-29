package fr.delphes.twitch.api.channelFollow.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelFollow(
    override val condition: ChannelFollowCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<ChannelFollowCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_FOLLOW
}