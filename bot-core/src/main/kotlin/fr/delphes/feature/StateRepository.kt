package fr.delphes.feature

import fr.delphes.utils.Repository

interface StateRepository<T : State>: Repository<T> {
    override suspend fun save(item : T)
    override suspend fun load(): T
}