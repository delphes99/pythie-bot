package fr.delphes.features

import fr.delphes.feature.State
import fr.delphes.feature.StateRepository

class TestStateRepository<T : State>(
    val initializer: () -> T
) : StateRepository<T> {
    override suspend fun save(item: T) {}

    override suspend fun load(): T {
        return initializer()
    }
}