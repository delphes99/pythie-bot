package fr.delphes.features.overlay

import kotlinx.serialization.Serializable

@Serializable
data class OverlayVoth(
    val user: String,
    val duration: String
)