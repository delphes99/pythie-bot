package fr.delphes.twitch.api.channelCheer

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelCheer.payload.ChannelCheerCondition
import fr.delphes.twitch.api.channelCheer.payload.ChannelCheerEventPayload
import fr.delphes.twitch.api.channelCheer.payload.SubscribeChannelCheer
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class ChannelCheerEventSubConfiguration(
    channel: TwitchChannel,
    listener: suspend (NewCheer) -> Unit
) : EventSubConfiguration<NewCheer, ChannelCheerEventPayload, ChannelCheerCondition>(
    channel,
    EventSubTopic.NEW_CHEER,
    listener
) {
    override fun transform(
        payload: ChannelCheerEventPayload
    ): NewCheer {
        return NewCheer(
            channel,
            payload.user_name?.let(::User),
            payload.bits,
            payload.message
        )
    }

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