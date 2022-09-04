package fr.delphes.descriptor.item

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
@SerialName("description-string")
data class StringDescriptor(
    val name: String,
    val value: String? = null
) : ItemDescriptor()