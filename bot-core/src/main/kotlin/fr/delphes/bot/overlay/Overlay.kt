package fr.delphes.bot.overlay

import kotlinx.serialization.Serializable

@Serializable
data class Overlay(
    val id: String,
    val title: String,
    val resolution: Resolution,
    val elements: List<OverlayElement<*>> = emptyList(),
)
