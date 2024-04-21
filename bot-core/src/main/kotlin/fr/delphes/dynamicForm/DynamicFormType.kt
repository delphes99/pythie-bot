package fr.delphes.dynamicForm

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class DynamicFormType(
    val id: String,
)