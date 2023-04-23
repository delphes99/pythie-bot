package fr.delphes.feature.descriptor

import kotlinx.serialization.Serializable

@Serializable
sealed class FeatureDescriptor {
    abstract val fieldName: String
    abstract val description: String
    abstract val value: Any?
}

