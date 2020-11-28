package fr.delphes.twitch.eventSub

import fr.delphes.twitch.api.channelUpdate.ChannelUpdate
import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateCondition
import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateEventPayload
import fr.delphes.twitch.api.newFollow.NewFollow
import fr.delphes.twitch.api.newFollow.payload.NewFollowCondition
import fr.delphes.twitch.api.newFollow.payload.NewFollowEventPayload
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.notification.NotificationSubscriptionPayload
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

sealed class EventSubConfiguration<MODEL, PAYLOAD, CONDITION : GenericCondition>(
    val type: EventSubSubscriptionType,
    webhookPathSuffix: String,
    private val listener: (MODEL) -> Unit
) {
    val callback = EventSubCallback(webhookPathSuffix, ::parse, ::notify)

    // Abstract to keep type instead of generics
    protected abstract suspend fun parse(call: ApplicationCall): NotificationPayload<PAYLOAD, CONDITION>

    private fun notify(event: PAYLOAD, subscription: NotificationSubscriptionPayload<CONDITION>) {
        val model = transform(event, subscription)
        listener(model)
    }

    protected abstract fun transform(payload: PAYLOAD, subscription: NotificationSubscriptionPayload<CONDITION>): MODEL
}

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
}

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

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelUpdateEventPayload, ChannelUpdateCondition> {
        return call.receive()
    }
}