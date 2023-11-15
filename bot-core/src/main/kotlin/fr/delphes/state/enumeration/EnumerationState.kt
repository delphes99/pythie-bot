package fr.delphes.state.enumeration

interface EnumerationState<T> {
    val id: EnumStateId

    suspend fun getItems(): List<EnumStateItem>

    fun deserialize(serializeValue: String): T
}