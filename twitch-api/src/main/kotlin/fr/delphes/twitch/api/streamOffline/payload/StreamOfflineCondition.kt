package fr.delphes.twitch.api.streamOffline.payload

import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class StreamOfflineCondition(
    val broadcaster_user_id: String
): GenericCondition