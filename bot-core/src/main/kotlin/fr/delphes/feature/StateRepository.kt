package fr.delphes.feature

import fr.delphes.utils.RepositoryWithInit

@Deprecated("migrate to fr.delphes.state.State")
interface StateRepository<T : State> : RepositoryWithInit<T> {
    override suspend fun save(item: T)
    override suspend fun load(): T
}