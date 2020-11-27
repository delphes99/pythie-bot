package fr.delphes.twitch.pubsub

import kotlinx.serialization.Serializable

@Serializable
data class ListenTo(
    val type: String,
    val nonce: String? = null,
    val data: ListenToData
)