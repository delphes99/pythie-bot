package fr.delphes.annotation.outgoingEvent

import fr.delphes.dynamicForm.FieldDescriptorMapper

object CustomFieldTypeMapper : FieldDescriptorMapper<CustomFieldType> {
    override suspend fun map(value: String): CustomFieldType {
        return CustomFieldType(value)
    }
}