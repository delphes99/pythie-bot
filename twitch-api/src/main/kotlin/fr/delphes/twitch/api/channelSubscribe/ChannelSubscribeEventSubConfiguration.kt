package fr.delphes.twitch.api.channelSubscribe

import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscribeCondition
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscribeEventPayload
import fr.delphes.twitch.api.channelSubscribe.payload.SubscribeChannelSubscribe
import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

class ChannelSubscribeEventSubConfiguration :
    EventSubConfiguration<ChannelSubscribeEventPayload, ChannelSubscribeCondition>(
        EventSubTopic.NEW_SUB
    ) {
    override fun subscribePayload(
        userId: UserId,
        transport: SubscribeTransport
    ) = SubscribeChannelSubscribe(
        ChannelSubscribeCondition(userId),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelSubscribeEventPayload, ChannelSubscribeCondition> {
        return call.receive()
    }
}