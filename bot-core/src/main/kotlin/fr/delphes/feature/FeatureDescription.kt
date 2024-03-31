package fr.delphes.feature

import fr.delphes.dynamicForm.descriptor.FieldDescriptor
import kotlinx.serialization.Serializable

@Serializable
data class FeatureDescription(
    val type: String,
    val id: String,
    val descriptors: List<FieldDescriptor>,
)