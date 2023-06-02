package fr.delphes.connector.obs.business

import kotlinx.serialization.Serializable

@Serializable
data class SourceFilter(
    val sourceName: String,
    val filterName: String,
)
