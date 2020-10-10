package fr.delphes.feature

interface HavePersistantState<T : State> : HaveState<T> {
    val stateRepository: StateRepository<T>

    fun save() {
        stateRepository.save(state)
    }

    fun load(): T {
        return stateRepository.load()
    }
}