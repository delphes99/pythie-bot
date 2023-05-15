package fr.delphes.feature

@Deprecated("migrate to fr.delphes.state.State")
interface HavePersistantState<T : State> : HaveState<T> {
    val stateRepository: StateRepository<T>

    suspend fun save() {
        stateRepository.save(state)
    }

    suspend fun load(): T {
        return stateRepository.load()
    }
}