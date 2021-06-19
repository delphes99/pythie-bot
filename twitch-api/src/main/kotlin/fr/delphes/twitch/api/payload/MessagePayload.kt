package fr.delphes.twitch.api.payload

import kotlinx.serialization.Serializable

@Serializable
data class MessagePayload(
    val emotes: List<MessageEmotePayload> = emptyList(),
    val text: String
)
