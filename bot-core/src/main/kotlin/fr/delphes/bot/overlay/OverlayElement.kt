package fr.delphes.bot.overlay

import kotlinx.serialization.Serializable

@Serializable
data class OverlayElement<T: OverlayElementProperties>(
    val general: OverlayElementGeneralProperties,
    val properties: T
)