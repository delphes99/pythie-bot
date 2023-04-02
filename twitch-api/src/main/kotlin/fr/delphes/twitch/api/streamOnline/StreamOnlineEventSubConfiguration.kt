package fr.delphes.twitch.api.streamOnline

import fr.delphes.twitch.api.streamOnline.payload.StreamOnlineCondition
import fr.delphes.twitch.api.streamOnline.payload.StreamOnlineEventPayload
import fr.delphes.twitch.api.streamOnline.payload.SubscribeStreamOnline
import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

class StreamOnlineEventSubConfiguration :
    EventSubConfiguration<StreamOnlineEventPayload, StreamOnlineCondition>(
        EventSubTopic.STREAM_ONLINE
    ) {
    override fun subscribePayload(
        userId: UserId,
        transport: SubscribeTransport
    ) = SubscribeStreamOnline(
        StreamOnlineCondition(userId),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<StreamOnlineEventPayload, StreamOnlineCondition> {
        return call.receive()
    }
}