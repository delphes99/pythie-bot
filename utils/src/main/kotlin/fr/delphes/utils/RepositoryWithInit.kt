package fr.delphes.utils

interface RepositoryWithInit<T>: Repository<T> {
    override suspend fun load(): T
}