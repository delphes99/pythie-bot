package fr.delphes.twitch.api.streamOffline

import fr.delphes.twitch.api.streamOffline.payload.StreamOfflineCondition
import fr.delphes.twitch.api.streamOffline.payload.StreamOfflineEventPayload
import fr.delphes.twitch.api.streamOffline.payload.SubscribeStreamOffline
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.notification.NotificationSubscriptionPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class StreamOfflineEventSubConfiguration(
    listener: (StreamOffline) -> Unit
) : EventSubConfiguration<StreamOffline, StreamOfflineEventPayload, StreamOfflineCondition>(
    EventSubSubscriptionType.STREAM_ONLINE,
    "StreamOnline",
    listener
) {
    override fun transform(
        payload: StreamOfflineEventPayload,
        subscription: NotificationSubscriptionPayload<StreamOfflineCondition>
    ): StreamOffline {
        return StreamOffline
    }

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): SubscribeStreamOffline {
        return SubscribeStreamOffline(
            StreamOfflineCondition(userId),
            transport
        )
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<StreamOfflineEventPayload, StreamOfflineCondition> {
        return call.receive()
    }
}