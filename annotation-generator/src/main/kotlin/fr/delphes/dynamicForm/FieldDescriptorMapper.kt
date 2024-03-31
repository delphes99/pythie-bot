package fr.delphes.dynamicForm

interface FieldDescriptorMapper<T> {
    suspend fun map(value: String): T
}
