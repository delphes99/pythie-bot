package fr.delphes.annotation.outgoingEvent.builder

object CustomFieldTypeMapper : FieldDescriptorMapper<CustomFieldType> {
    override fun map(value: String): CustomFieldType {
        return CustomFieldType(value)
    }
}