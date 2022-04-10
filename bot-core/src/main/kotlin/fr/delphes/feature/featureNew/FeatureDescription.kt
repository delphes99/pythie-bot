package fr.delphes.feature.featureNew

import kotlinx.serialization.Serializable

@Serializable
data class FeatureDescription(
    val identifier: FeatureIdentifier,
    val type: FeatureType,
    val items: List<FeatureDescriptionItem>
)