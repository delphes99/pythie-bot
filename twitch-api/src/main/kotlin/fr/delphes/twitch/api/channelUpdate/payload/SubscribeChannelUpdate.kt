package fr.delphes.twitch.api.channelUpdate.payload

import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import kotlinx.serialization.Serializable

@Serializable
class SubscribeChannelUpdate(
    override val condition: ChannelUpdateCondition,
    override val transport: SubscribeTransport,
    override val type: EventSubSubscriptionType = EventSubSubscriptionType.CHANNEL_UPDATE,
    override val version: String = "1",
): EventSubSubscribe<ChannelUpdateCondition>