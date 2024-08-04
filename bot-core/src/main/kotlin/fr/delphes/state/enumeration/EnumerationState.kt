package fr.delphes.state.enumeration

import fr.delphes.state.State
import fr.delphes.state.StateId

interface EnumerationState<T> : State {
    override val id: StateId<out State>

    suspend fun getItems(): List<EnumStateItem>

    fun deserialize(serializeValue: String): T
}