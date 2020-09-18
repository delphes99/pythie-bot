package fr.delphes.feature

interface HaveState<T : State> {
    val state : T
    val stateRepository: StateRepository<T>

    fun save() {
        stateRepository.save(state)
    }

    fun load(): T {
        return stateRepository.load()
    }
}