package fr.delphes.feature

import fr.delphes.feature.descriptor.FeatureDescriptor
import kotlinx.serialization.Serializable

@Serializable
data class OutgoingEventBuilderDescription(
    val type: String,
    val descriptors: List<FeatureDescriptor>,
)