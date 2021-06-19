package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelPollProgress(
    override val condition: ChannelPollProgressCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<ChannelPollProgressCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_POLL_PROGRESS
}