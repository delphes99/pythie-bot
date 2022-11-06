package fr.delphes.bot.overlay

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Image")
data class OverlayImageElement(
    val url: String,
    val width: Long? = null,
    val height: Long? = null
) : OverlayElementProperties()