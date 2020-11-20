package fr.delphes.feature

class TestStateRepository<T : State>(
    val initializer: () -> T
) : StateRepository<T> {
    override suspend fun save(state: T) {}

    override suspend fun load(): T {
        return initializer()
    }
}