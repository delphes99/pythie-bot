package fr.delphes.feature

@Deprecated("migrate to fr.delphes.state.State")
interface HaveState<T : State> {
    val state: T
}