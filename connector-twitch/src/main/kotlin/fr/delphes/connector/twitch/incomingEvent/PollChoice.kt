package fr.delphes.connector.twitch.incomingEvent

import kotlinx.serialization.Serializable

@Serializable
data class PollChoice(
    val value: String,
)
