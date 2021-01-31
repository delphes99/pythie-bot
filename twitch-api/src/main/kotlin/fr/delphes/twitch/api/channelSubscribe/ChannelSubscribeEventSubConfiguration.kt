package fr.delphes.twitch.api.channelSubscribe

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscribeCondition
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscribeEventPayload
import fr.delphes.twitch.api.channelSubscribe.payload.SubscribeChannelSubscribe
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class ChannelSubscribeEventSubConfiguration(
    channel: TwitchChannel,
    listener: (NewSub) -> Unit
) : EventSubConfiguration<NewSub, ChannelSubscribeEventPayload, ChannelSubscribeCondition>(
    channel,
    "newSub",
    listener
) {
    override fun transform(
        payload: ChannelSubscribeEventPayload
    ): NewSub {
        return NewSub(
            channel,
            User(payload.user_name),
            payload.tier,
            payload.is_gift
        )
    }

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): SubscribeChannelSubscribe {
        return SubscribeChannelSubscribe(
            ChannelSubscribeCondition(userId),
            transport
        )
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelSubscribeEventPayload, ChannelSubscribeCondition> {
        return call.receive()
    }
}