package fr.delphes.feature

interface HaveState<T : State> {
    val state : T
    val stateManager: StateManager<T>

    fun save() {
        stateManager.save(state)
    }

    fun load(): T {
        return stateManager.load()
    }
}