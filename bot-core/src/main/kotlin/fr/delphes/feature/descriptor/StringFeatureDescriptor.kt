package fr.delphes.feature.descriptor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("STRING")
@Serializable
class StringFeatureDescriptor(
    override val fieldName: String,
    override val description: String,
    override val value: String?,
) : FeatureDescriptor()