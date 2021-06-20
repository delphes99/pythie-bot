package fr.delphes.utils.store

class Reducer<STATE, ACTION>(
    private val stateMutation: (STATE) -> STATE,
    private val klass: Class<ACTION>
) {
    fun applyOn(state: STATE, action: Action): STATE {
        return if(action::class.java == klass) {
            stateMutation(state)
        } else {
            state
        }
    }
}