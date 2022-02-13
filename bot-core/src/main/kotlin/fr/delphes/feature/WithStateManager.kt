package fr.delphes.feature

interface WithStateManager<T : State> {
    var state: T
    suspend fun newState(newState : T)
}