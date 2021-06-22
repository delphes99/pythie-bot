package fr.delphes.utils.store

fun interface Reducer<STATE, ACTION> {
    fun apply(action: ACTION, state: STATE) : STATE
}