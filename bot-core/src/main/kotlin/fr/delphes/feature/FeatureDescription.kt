package fr.delphes.feature

import kotlinx.serialization.Serializable

@Serializable
data class FeatureDescription(
    val type: String,
    val descriptors: List<FeatureDescriptor>,
)