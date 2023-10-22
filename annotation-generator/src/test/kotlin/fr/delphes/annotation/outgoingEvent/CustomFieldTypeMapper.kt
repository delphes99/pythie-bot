package fr.delphes.annotation.outgoingEvent

import fr.delphes.annotation.outgoingEvent.createBuilder.FieldDescriptorMapper

object CustomFieldTypeMapper : FieldDescriptorMapper<CustomFieldType> {
    override fun map(value: String): CustomFieldType {
        return CustomFieldType(value)
    }
}