package fr.delphes.utils.store

open class Reducer<STATE, ACTION>(
    private val stateMutation: (STATE, ACTION) -> STATE,
    private val klass: Class<ACTION>
) {
    fun applyOn(state: STATE, action: Action): STATE {
        return if(action::class.java == klass) {
            stateMutation(state, action as ACTION)
        } else {
            state
        }
    }
}