package fr.delphes.twitch.eventSub.payload

import kotlinx.serialization.Serializable

@Serializable
data class Condition(
    val broadcaster_user_id: String
)