package fr.delphes.feature

interface StateRepository<T : State> {
    fun save(state : T)
    fun load(): T
}