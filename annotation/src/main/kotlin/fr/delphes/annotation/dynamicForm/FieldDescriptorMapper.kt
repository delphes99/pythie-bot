package fr.delphes.annotation.dynamicForm

interface FieldDescriptorMapper<T> {
    fun mapFromDto(value: String): T
    fun mapToDto(value: T): String
}
