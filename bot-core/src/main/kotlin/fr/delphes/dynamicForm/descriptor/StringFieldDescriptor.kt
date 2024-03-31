package fr.delphes.dynamicForm.descriptor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("STRING")
@Serializable
data class StringFieldDescriptor(
    override val fieldName: String,
    override val description: String,
    override val value: String?,
) : FieldDescriptor()