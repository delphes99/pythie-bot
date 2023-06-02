package fr.delphes.connector.twitch.incomingEvent

import kotlinx.serialization.Serializable

@Serializable
data class Poll(
    val id: String,
    val title: String,
    val choices: List<PollChoice>,
)