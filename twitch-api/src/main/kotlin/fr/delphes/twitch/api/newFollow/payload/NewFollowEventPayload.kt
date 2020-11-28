package fr.delphes.twitch.api.newFollow.payload

import kotlinx.serialization.Serializable

@Serializable
data class NewFollowEventPayload(
    val user_id: String,
    val user_name: String,
    val broadcaster_user_id: String,
    val broadcaster_user_name: String
)