package fr.delphes.annotation.outgoingEvent.builder

interface FieldDescriptorMapper<T> {
    fun map(value: String): T
}
