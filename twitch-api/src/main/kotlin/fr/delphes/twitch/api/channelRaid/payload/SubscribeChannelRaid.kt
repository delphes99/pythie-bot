package fr.delphes.twitch.api.channelRaid.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelRaid(
    override val condition: ChannelRaidCondition,
    override val transport: SubscribeTransport,
    override val version: String = "1"
) : EventSubSubscribe<ChannelRaidCondition> {
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_RAID
}