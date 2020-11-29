package fr.delphes.twitch.api.streamOnline

import fr.delphes.twitch.api.streamOnline.payload.StreamOnlineCondition
import fr.delphes.twitch.api.streamOnline.payload.StreamOnlineEventPayload
import fr.delphes.twitch.api.streamOnline.payload.SubscribeStreamOnline
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.notification.NotificationSubscriptionPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class StreamOnlineEventSubConfiguration(
    listener: (StreamOnline) -> Unit
) : EventSubConfiguration<StreamOnline, StreamOnlineEventPayload, StreamOnlineCondition>(
    EventSubSubscriptionType.STREAM_ONLINE,
    "StreamOnline",
    listener
) {
    override fun transform(
        payload: StreamOnlineEventPayload,
        subscription: NotificationSubscriptionPayload<StreamOnlineCondition>
    ): StreamOnline {
        return StreamOnline(payload.type)
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<StreamOnlineEventPayload, StreamOnlineCondition> {
        return call.receive()
    }

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): SubscribeStreamOnline {
        return SubscribeStreamOnline(
            StreamOnlineCondition(userId),
            transport
        )
    }
}