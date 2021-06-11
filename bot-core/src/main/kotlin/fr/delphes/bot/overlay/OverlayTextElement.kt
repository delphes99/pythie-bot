package fr.delphes.bot.overlay

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Text")
data class OverlayTextElement(
    val id: String,
    val left: Int,
    val top: Int,
    val text: String
) : OverlayElement()