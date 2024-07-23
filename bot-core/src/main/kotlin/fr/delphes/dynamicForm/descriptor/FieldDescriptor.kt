package fr.delphes.dynamicForm.descriptor

import kotlinx.serialization.Serializable

@Serializable
sealed class FieldDescriptor {
    abstract val fieldName: String
    abstract val description: String
    abstract val value: Any?
}

