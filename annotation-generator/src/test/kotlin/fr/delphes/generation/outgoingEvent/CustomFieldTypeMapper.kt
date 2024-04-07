package fr.delphes.generation.outgoingEvent

import fr.delphes.generation.dynamicForm.FieldDescriptorMapper

object CustomFieldTypeMapper : FieldDescriptorMapper<CustomFieldType> {
    override suspend fun map(value: String): CustomFieldType {
        return CustomFieldType(value)
    }
}