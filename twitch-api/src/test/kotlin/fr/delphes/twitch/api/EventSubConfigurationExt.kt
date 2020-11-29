package fr.delphes.twitch.api

import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.GenericCondition
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.serialization.Serializer
import kotlinx.serialization.decodeFromString

internal inline fun <reified EVENT, reified CONDITION: GenericCondition, MODEL> EventSubConfiguration<MODEL, EVENT, CONDITION>.parseToModel(
    payloadStr: String
): MODEL {
    val payload =
        Serializer.decodeFromString<NotificationPayload<EVENT, CONDITION>>(
            payloadStr
        ).event!!
    return transform(payload)
}