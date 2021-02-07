package fr.delphes.twitch.eventSub

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall

abstract class EventSubConfiguration<MODEL, PAYLOAD, CONDITION : GenericCondition>(
    protected val channel: TwitchChannel,
    webhookPathSuffix: String,
    private val listener: suspend (MODEL) -> Unit
) {
    val callback = EventSubCallback(webhookPathSuffix, ::parse, ::notify)

    private suspend fun notify(event: PAYLOAD) {
        transform(event)?.also { listener(it) }
    }

    internal abstract fun transform(payload: PAYLOAD): MODEL?

    abstract fun subscribePayload(userId: String, transport: SubscribeTransport): EventSubSubscribe<CONDITION>

    // Abstract to keep type instead of generics
    protected abstract suspend fun parse(call: ApplicationCall): NotificationPayload<PAYLOAD, CONDITION>
}