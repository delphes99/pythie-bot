package fr.delphes.feature.overlay

import kotlinx.serialization.Serializable

@Serializable
data class OverlayInfos(
    val last_follow: List<String> = emptyList(),
    val last_voth: List<String> = emptyList()
)