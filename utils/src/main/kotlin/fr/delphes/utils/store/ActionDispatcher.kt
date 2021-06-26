package fr.delphes.utils.store

fun interface ActionDispatcher {
    fun applyAction(action: Action)
}