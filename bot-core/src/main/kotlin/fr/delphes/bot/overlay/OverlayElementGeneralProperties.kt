package fr.delphes.bot.overlay

import kotlinx.serialization.Serializable

@Serializable
data class OverlayElementGeneralProperties(
    val id: String,
    val left: Int,
    val top: Int,
)
