package fr.delphes.twitch.api.channelUpdate

import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateCondition
import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateEventPayload
import fr.delphes.twitch.api.channelUpdate.payload.SubscribeChannelUpdate
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.notification.NotificationSubscriptionPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class ChannelUpdateEventSubConfiguration(
    listener: (ChannelUpdate) -> Unit
) : EventSubConfiguration<ChannelUpdate, ChannelUpdateEventPayload, ChannelUpdateCondition>(
    EventSubSubscriptionType.CHANNEL_UPDATE,
    "channelUpdate",
    listener
) {
    override fun transform(
        payload: ChannelUpdateEventPayload,
        subscription: NotificationSubscriptionPayload<ChannelUpdateCondition>
    ): ChannelUpdate {
        return ChannelUpdate(payload.title, payload.language, payload.category_id, payload.category_name)
    }

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): SubscribeChannelUpdate {
        return SubscribeChannelUpdate(
            ChannelUpdateCondition(userId),
            transport
        )
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelUpdateEventPayload, ChannelUpdateCondition> {
        return call.receive()
    }
}