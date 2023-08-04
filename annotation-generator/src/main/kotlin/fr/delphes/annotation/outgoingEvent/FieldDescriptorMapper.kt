package fr.delphes.annotation.outgoingEvent

interface FieldDescriptorMapper<T> {
    fun map(value: String): T
}
