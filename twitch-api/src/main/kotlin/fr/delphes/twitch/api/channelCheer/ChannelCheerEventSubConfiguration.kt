package fr.delphes.twitch.api.channelCheer

import fr.delphes.twitch.api.channelCheer.payload.ChannelCheerCondition
import fr.delphes.twitch.api.channelCheer.payload.ChannelCheerEventPayload
import fr.delphes.twitch.api.channelCheer.payload.SubscribeChannelCheer
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class ChannelCheerEventSubConfiguration :
    EventSubConfiguration<
            ChannelCheerEventPayload,
            ChannelCheerCondition>(
        EventSubTopic.NEW_CHEER
    ) {
    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): SubscribeChannelCheer {
        return SubscribeChannelCheer(
            ChannelCheerCondition(userId),
            transport
        )
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelCheerEventPayload, ChannelCheerCondition> {
        return call.receive()
    }
}