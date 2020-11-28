package fr.delphes.twitch.api.newFollow.payload

import fr.delphes.twitch.eventSub.payload.EventSubSubscriptionPayload
import kotlinx.serialization.Serializable

@Serializable
data class NewFollowPayload(
    val challenge: String? = null,
    val subscription: EventSubSubscriptionPayload,
    val event: NewFollowEventPayload? = null
)