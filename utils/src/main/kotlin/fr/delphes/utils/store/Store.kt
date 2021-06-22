package fr.delphes.utils.store

class Store<T>(
    private var _currentState: T,
    private val reducers: List<ReducerWrapper<T, *>>
) {
    constructor(initialState: T,
                vararg reducers: ReducerWrapper<T, *>) : this(initialState, listOf(*reducers))

    fun dispatch(action: Action) {
        _currentState = reducers.fold(_currentState) { state, reducer -> reducer.applyOn(state, action) }
    }

    val currentState: T get() = _currentState
}