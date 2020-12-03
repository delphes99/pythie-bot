package fr.delphes.feature

class TestStateRepository<T : State>(
    val initializer: () -> T
) : StateRepository<T> {
    override suspend fun save(item: T) {}

    override suspend fun load(): T {
        return initializer()
    }
}