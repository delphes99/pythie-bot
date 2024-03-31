package fr.delphes.feature

import fr.delphes.dynamicForm.descriptor.FieldDescriptor
import kotlinx.serialization.Serializable

@Serializable
data class OutgoingEventBuilderDescription(
    val type: OutgoingEventType,
    val descriptors: List<FieldDescriptor>,
) {
    constructor(
        type: OutgoingEventType,
        vararg descriptors: FieldDescriptor,
    ) : this(
        type = type,
        descriptors = descriptors.toList()
    )
}