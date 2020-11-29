package fr.delphes.twitch.api.streamOffline.payload

import kotlinx.serialization.Serializable

@Serializable
data class StreamOfflineEventPayload(
    val broadcaster_user_id: String,
    val broadcaster_user_name: String
)