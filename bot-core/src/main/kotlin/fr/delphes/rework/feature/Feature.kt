package fr.delphes.rework.feature

import kotlinx.serialization.Serializable

@Serializable
data class Feature(
    val id: FeatureId,
    val definition: FeatureDefinition,
)
