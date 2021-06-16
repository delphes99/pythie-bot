package fr.delphes.twitch.api.payload

import kotlinx.serialization.Serializable

@Serializable
data class MessagePayload(
    val emotes: List<MessageEmotePayload>,
    val text: String
)
