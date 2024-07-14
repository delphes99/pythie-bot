package fr.delphes.generation.dynamicForm

interface FieldDescriptorMapper<T> {
    fun mapFromDto(value: String): T
    fun mapToDto(value: T): String
}
