package fr.delphes.feature

interface StateManager<T : State> {
    fun save(state : T)
    fun load(): T
}