package fr.delphes.twitch.eventSub

import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionType
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.notification.NotificationSubscriptionPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall

abstract class EventSubConfiguration<MODEL, PAYLOAD, CONDITION : GenericCondition>(
    val type: EventSubSubscriptionType,
    webhookPathSuffix: String,
    private val listener: (MODEL) -> Unit
) {
    val callback = EventSubCallback(webhookPathSuffix, ::parse, ::notify)

    private fun notify(event: PAYLOAD, subscription: NotificationSubscriptionPayload<CONDITION>) {
        val model = transform(event, subscription)
        listener(model)
    }

    protected abstract fun transform(payload: PAYLOAD, subscription: NotificationSubscriptionPayload<CONDITION>): MODEL

    abstract fun subscribePayload(userId: String, transport: SubscribeTransport): EventSubSubscribe<CONDITION>

    // Abstract to keep type instead of generics
    protected abstract suspend fun parse(call: ApplicationCall): NotificationPayload<PAYLOAD, CONDITION>
}