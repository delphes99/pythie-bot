package fr.delphes.generation.dynamicForm.metadata

import fr.delphes.generation.dynamicForm.FieldDescriptorMapper

object CustomFieldTypeMapper : FieldDescriptorMapper<CustomFieldType> {
    override fun mapFromDto(value: String): CustomFieldType {
        return CustomFieldType(value)
    }

    override fun mapToDto(value: CustomFieldType): String {
        return value.value
    }
}