package fr.delphes.dynamicForm

import fr.delphes.dynamicForm.descriptor.FieldDescriptor
import kotlinx.serialization.Serializable

@Serializable
data class DynamicFormDescription(
    val type: DynamicFormType,
    val descriptors: List<FieldDescriptor>,
) {
    constructor(
        type: DynamicFormType,
        vararg descriptors: FieldDescriptor,
    ) : this(
        type = type, descriptors = descriptors.toList()
    )
}
