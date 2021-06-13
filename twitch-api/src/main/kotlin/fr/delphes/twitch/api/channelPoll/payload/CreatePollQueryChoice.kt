package fr.delphes.twitch.api.channelPoll.payload

import kotlinx.serialization.Serializable

@Serializable
data class CreatePollQueryChoice(
    val title: String
)