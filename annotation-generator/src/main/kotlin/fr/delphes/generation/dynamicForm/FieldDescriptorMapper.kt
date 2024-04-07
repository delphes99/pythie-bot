package fr.delphes.generation.dynamicForm

interface FieldDescriptorMapper<T> {
    suspend fun map(value: String): T
}
