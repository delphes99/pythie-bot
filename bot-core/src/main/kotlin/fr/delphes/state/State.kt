package fr.delphes.state

interface State {
    val id: StateId<out State>
}