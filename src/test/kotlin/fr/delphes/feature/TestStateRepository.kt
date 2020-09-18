package fr.delphes.feature

class TestStateRepository<T : State>(
    val initializer: () -> T
) : StateRepository<T> {
    override fun save(state: T) {}

    override fun load(): T {
        return initializer()
    }
}