package fr.delphes.twitch.eventSub.payload.notification

import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class NotificationPayload<TYPE, CONDITION : GenericCondition>(
    val challenge: String? = null,
    val subscription: NotificationSubscriptionPayload<CONDITION>,
    val event: TYPE? = null
)