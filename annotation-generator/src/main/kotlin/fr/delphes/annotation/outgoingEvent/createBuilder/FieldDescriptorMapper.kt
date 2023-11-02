package fr.delphes.annotation.outgoingEvent.createBuilder

import fr.delphes.state.StateProvider

interface FieldDescriptorMapper<T> {
    suspend fun map(value: String, stateProvider: StateProvider): T
}
