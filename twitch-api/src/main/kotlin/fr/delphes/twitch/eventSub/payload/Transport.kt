package fr.delphes.twitch.eventSub.payload

import kotlinx.serialization.Serializable

@Serializable
data class Transport(
    val method: String = "webhook",
    val callback: String
)