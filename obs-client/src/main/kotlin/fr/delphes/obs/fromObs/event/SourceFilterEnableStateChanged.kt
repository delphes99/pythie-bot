package fr.delphes.obs.fromObs.event

import kotlinx.serialization.Serializable

@Serializable
data class SourceFilterEnableStateChanged(
    val filterEnabled: Boolean,
    val filterName: String,
    val sourceName: String,
) : Event