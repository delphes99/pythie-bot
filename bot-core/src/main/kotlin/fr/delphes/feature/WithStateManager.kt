package fr.delphes.feature

@Deprecated("migrate to fr.delphes.state.State")
interface WithStateManager<T : State> {
    var state: T
    suspend fun newState(newState: T)
}