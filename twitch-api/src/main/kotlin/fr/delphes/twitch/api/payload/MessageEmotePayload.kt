package fr.delphes.twitch.api.payload

import kotlinx.serialization.Serializable

@Serializable
data class MessageEmotePayload(
    val begin: Int,
    val end: Int,
    val id: String
)
