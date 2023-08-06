package fr.delphes.annotation.outgoingEvent.createBuilder

interface FieldDescriptorMapper<T> {
    fun map(value: String): T
}
