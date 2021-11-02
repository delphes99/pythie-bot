package fr.delphes.utils

interface Repository<T> {
    suspend fun save(item: T)
    suspend fun load(): T?
}