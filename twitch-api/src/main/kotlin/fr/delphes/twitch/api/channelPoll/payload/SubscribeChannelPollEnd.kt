package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowCondition
import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelPollEnd(
    override val condition: ChannelPollEndCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
): EventSubSubscribe<ChannelPollEndCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_POLL_END
}