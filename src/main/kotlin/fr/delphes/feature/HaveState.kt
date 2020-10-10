package fr.delphes.feature

interface HaveState<T: State> {
    val state : T
}