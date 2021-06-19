package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelPollBegin(
    override val condition: ChannelPollBeginCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<ChannelPollBeginCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_POLL_BEGIN
}