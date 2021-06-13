package fr.delphes.twitch.api.channelSubscribe.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelSubscriptionMessage(
    override val condition: ChannelSubscriptionMessageCondition,
    override val transport: SubscribeTransport,
    override val version: String = "beta"
): EventSubSubscribe<ChannelSubscriptionMessageCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_SUBSCRIPTION_MESSAGE
}