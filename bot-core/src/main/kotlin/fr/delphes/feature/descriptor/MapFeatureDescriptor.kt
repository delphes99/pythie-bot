package fr.delphes.feature.descriptor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("MAP")
@Serializable
data class MapFeatureDescriptor(
    override val fieldName: String,
    override val description: String,
    override val value: Map<String, String>,
) : FeatureDescriptor()