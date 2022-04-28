package fr.delphes.twitch.eventSub

import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall

abstract class EventSubConfiguration<PAYLOAD, CONDITION : GenericCondition>(
    topic: EventSubTopic
) {
    val callback = EventSubCallback(topic, ::parse)

    abstract fun subscribePayload(userId: String, transport: SubscribeTransport): EventSubSubscribe<CONDITION>

    // Abstract to keep type instead of generics
    protected abstract suspend fun parse(call: ApplicationCall): NotificationPayload<PAYLOAD, CONDITION>
}