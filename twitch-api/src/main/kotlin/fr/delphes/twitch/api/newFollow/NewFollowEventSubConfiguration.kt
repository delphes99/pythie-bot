package fr.delphes.twitch.api.newFollow

import fr.delphes.twitch.api.newFollow.payload.NewFollowCondition
import fr.delphes.twitch.api.newFollow.payload.NewFollowEventPayload
import fr.delphes.twitch.api.newFollow.payload.SubscribeNewFollow
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive
import java.time.LocalDateTime

class NewFollowEventSubConfiguration(
    listener: (NewFollow) -> Unit
) : EventSubConfiguration<NewFollow, NewFollowEventPayload, NewFollowCondition>(
    "newFollow",
    listener
) {
    override fun transform(
        payload: NewFollowEventPayload
    ): NewFollow {
        return NewFollow(User(payload.user_name), LocalDateTime.now())
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

    override suspend fun parse(call: ApplicationCall): NotificationPayload<NewFollowEventPayload, NewFollowCondition> {
        return call.receive()
    }
}