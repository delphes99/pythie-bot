package fr.delphes.feature

import kotlinx.serialization.Serializable

@Serializable
data class FeatureDescriptor(
    val fieldName: String,
    val description: String,
    val type: FeatureDescriptionType,
    val value: String? = null
)
