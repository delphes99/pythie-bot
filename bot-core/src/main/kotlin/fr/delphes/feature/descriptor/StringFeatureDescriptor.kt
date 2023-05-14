package fr.delphes.feature.descriptor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("STRING")
@Serializable
data class StringFeatureDescriptor(
    override val fieldName: String,
    override val description: String,
    override val value: String?,
) : FeatureDescriptor()