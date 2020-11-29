package fr.delphes.twitch.api.channelSubscribe.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelSubscribe(
    override val condition: ChannelSubscribeCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<ChannelSubscribeCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_SUBSCRIBE
}