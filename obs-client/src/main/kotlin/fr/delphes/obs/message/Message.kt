package fr.delphes.obs.message

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Message(
    val op: Int,
    val d: JsonElement
)