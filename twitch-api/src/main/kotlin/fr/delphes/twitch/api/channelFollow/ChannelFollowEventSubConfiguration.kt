package fr.delphes.twitch.api.channelFollow

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowCondition
import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowEventPayload
import fr.delphes.twitch.api.channelFollow.payload.SubscribeChannelFollow
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class ChannelFollowEventSubConfiguration(
    channel: TwitchChannel,
    listener: suspend (NewFollow) -> Unit
) : EventSubConfiguration<NewFollow, ChannelFollowEventPayload, ChannelFollowCondition>(
    channel,
    EventSubTopic.NEW_FOLLOW,
    listener
) {
    override fun transform(
        payload: ChannelFollowEventPayload
    ): NewFollow {
        return NewFollow(
            channel,
            User(payload.user_name)
        )
    }

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): SubscribeChannelFollow {
        return SubscribeChannelFollow(
            ChannelFollowCondition(userId),
            transport
        )
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelFollowEventPayload, ChannelFollowCondition> {
        return call.receive()
    }
}