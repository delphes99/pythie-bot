package fr.delphes.feature

import fr.delphes.feature.descriptor.FeatureDescriptor
import kotlinx.serialization.Serializable

@Serializable
data class FeatureDescription(
    val type: String,
    val id: String,
    val descriptors: List<FeatureDescriptor>,
)