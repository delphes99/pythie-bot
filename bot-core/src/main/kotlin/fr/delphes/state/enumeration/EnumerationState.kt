package fr.delphes.state.enumeration

import fr.delphes.state.State

interface EnumerationState : State {
    suspend fun getItems(): List<EnumStateItem>
}