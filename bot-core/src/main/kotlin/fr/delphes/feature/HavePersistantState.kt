package fr.delphes.feature

interface HavePersistantState<T : State> : HaveState<T> {
    val stateRepository: StateRepository<T>

    suspend fun save() {
        stateRepository.save(state)
    }

    suspend fun load(): T {
        return stateRepository.load()
    }
}