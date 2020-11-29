package fr.delphes.twitch.api.streamOnline.payload

import fr.delphes.twitch.api.streamOnline.StreamType
import kotlinx.serialization.Serializable

@Serializable
data class StreamOnlineEventPayload(
    val id: String,
    val broadcaster_user_id: String,
    val broadcaster_user_name: String,
    val type: StreamType
)