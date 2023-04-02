package fr.delphes.twitch.api.channelFollow

import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowCondition
import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowEventPayload
import fr.delphes.twitch.api.channelFollow.payload.SubscribeChannelFollow
import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

class ChannelFollowEventSubConfiguration :
    EventSubConfiguration<ChannelFollowEventPayload, ChannelFollowCondition>(
        EventSubTopic.NEW_FOLLOW
    ) {
    override fun subscribePayload(
        userId: UserId,
        transport: SubscribeTransport
    ) = SubscribeChannelFollow(
        ChannelFollowCondition(userId),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelFollowEventPayload, ChannelFollowCondition> {
        return call.receive()
    }
}