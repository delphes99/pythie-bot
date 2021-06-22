package fr.delphes.utils.store

open class ReducerWrapper<STATE, ACTION>(
    private val stateMutation: Reducer<STATE, ACTION>,
    private val klass: Class<ACTION>
) {
    companion object {
        inline fun <STATE, reified ACTION> of(stateMutation: Reducer<STATE, ACTION>): ReducerWrapper<STATE, ACTION> {
            return ReducerWrapper(stateMutation, ACTION::class.java)
        }
    }

    fun applyOn(state: STATE, action: Action): STATE {
        return if (action::class.java == klass) {
            stateMutation.apply(action as ACTION, state)
        } else {
            state
        }
    }
}

inline fun <STATE, reified ACTION> Reducer<STATE, ACTION>.wrap(): ReducerWrapper<STATE, ACTION> {
    return ReducerWrapper.of(this)
}