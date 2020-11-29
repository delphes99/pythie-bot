package fr.delphes.twitch.api.streamOnline.payload

import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class StreamOnlineCondition(
    val broadcaster_user_id: String
): GenericCondition