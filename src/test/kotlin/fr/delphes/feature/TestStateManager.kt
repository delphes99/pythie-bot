package fr.delphes.feature

class TestStateManager<T : State> : StateManager<T> {
    override fun save(state: T) {}

    override fun load(): T {
        TODO("Not yet implemented")
    }
}