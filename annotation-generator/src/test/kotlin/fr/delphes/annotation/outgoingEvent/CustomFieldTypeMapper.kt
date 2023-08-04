package fr.delphes.annotation.outgoingEvent

class CustomFieldTypeMapper : FieldDescriptorMapper<String> {
    override fun map(value: String): String {
        return value
    }
}