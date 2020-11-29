package fr.delphes.twitch.api.newFollow

import fr.delphes.twitch.api.newFollow.payload.NewFollowCondition
import fr.delphes.twitch.api.newFollow.payload.NewFollowEventPayload
import fr.delphes.twitch.api.newFollow.payload.SubscribeNewFollow
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.notification.NotificationSubscriptionPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class NewFollowEventSubConfiguration(
    listener: (NewFollow) -> Unit
) : EventSubConfiguration<NewFollow, NewFollowEventPayload, NewFollowCondition>(
    EventSubSubscriptionType.CHANNEL_FOLLOW,
    "newFollow",
    listener
) {
    override fun transform(
        payload: NewFollowEventPayload,
        subscription: NotificationSubscriptionPayload<NewFollowCondition>
    ): NewFollow {
        return NewFollow(User(payload.user_name), subscription.created_at)
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<NewFollowEventPayload, NewFollowCondition> {
        return call.receive()
    }

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): SubscribeNewFollow {
        return SubscribeNewFollow(
            NewFollowCondition(userId),
            transport
        )
    }
}