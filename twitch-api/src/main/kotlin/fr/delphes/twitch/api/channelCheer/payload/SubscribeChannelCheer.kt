package fr.delphes.twitch.api.channelCheer.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelCheer(
    override val condition: ChannelCheerCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<ChannelCheerCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_CHEER
}