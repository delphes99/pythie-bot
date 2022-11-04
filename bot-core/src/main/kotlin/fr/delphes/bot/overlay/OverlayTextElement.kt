package fr.delphes.bot.overlay

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Text")
data class OverlayTextElement(
    val text: String,
    val color: String = "#000000",
    val font: String = "Roboto",
    val fontSize: String = "20",
) : OverlayElementProperties()