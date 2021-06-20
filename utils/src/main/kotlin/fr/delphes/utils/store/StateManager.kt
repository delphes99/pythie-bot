package fr.delphes.utils.store

class StateManager<T>(
    private var _currentState: T,
    private val reducers: List<Reducer<T, *>>
) {
    constructor(initialState: T,
                vararg reducers: Reducer<T, *>) : this(initialState, listOf(*reducers))

    fun handle(action: Action) {
        reducers.fold(_currentState) { state, reducer -> reducer.applyOn(state, action) }
    }

    val currentState: T get() = _currentState
}