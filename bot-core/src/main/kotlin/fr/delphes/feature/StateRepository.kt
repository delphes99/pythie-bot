package fr.delphes.feature

import fr.delphes.utils.RepositoryWithInit

interface StateRepository<T : State>: RepositoryWithInit<T> {
    override suspend fun save(item : T)
    override suspend fun load(): T
}