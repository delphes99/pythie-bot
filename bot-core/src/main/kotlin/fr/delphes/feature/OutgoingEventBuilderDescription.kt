package fr.delphes.feature

import fr.delphes.feature.descriptor.FeatureDescriptor
import kotlinx.serialization.Serializable

@Serializable
data class OutgoingEventBuilderDescription(
    val type: OutgoingEventType,
    val descriptors: List<FeatureDescriptor>,
) {
    constructor(
        type: OutgoingEventType,
        vararg descriptors: FeatureDescriptor,
    ) : this(
        type = type,
        descriptors = descriptors.toList()
    )
}