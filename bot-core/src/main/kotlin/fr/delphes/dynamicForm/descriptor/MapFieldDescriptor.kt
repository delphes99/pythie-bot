package fr.delphes.dynamicForm.descriptor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("MAP")
@Serializable
data class MapFieldDescriptor(
    override val fieldName: String,
    override val description: String,
    override val value: Map<String, String>,
) : FieldDescriptor()