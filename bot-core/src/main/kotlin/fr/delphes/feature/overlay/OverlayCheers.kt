package fr.delphes.feature.overlay

import kotlinx.serialization.Serializable

@Serializable
data class OverlayCheers(
    val user: String?,
    val bits: Long
)