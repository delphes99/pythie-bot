package fr.delphes.feature.featureNew

@kotlinx.serialization.Serializable
data class FeatureCreation(
    val name: FeatureIdentifier,
    val type: FeatureType
)
