package fr.delphes.feature.descriptor

import fr.delphes.annotation.RegisterFieldDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("STRING")
@Serializable
@RegisterFieldDescriptor
data class StringFeatureDescriptor(
    override val fieldName: String,
    override val description: String,
    override val value: String?,

    ) : FeatureDescriptor()