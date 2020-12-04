package fr.delphes.feature.overlay

import kotlinx.serialization.Serializable

@Serializable
data class OverlayInfos(
    val last_follows: List<String> = emptyList(),
    val last_subs: List<String> = emptyList(),
    val last_cheers: List<OverlayCheers> = emptyList(),
    val last_voths: List<String> = emptyList()
)