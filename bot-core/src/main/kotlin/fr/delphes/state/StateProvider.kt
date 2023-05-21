package fr.delphes.state

class StateProvider(
    @PublishedApi
    internal val stateManager: StateManager,
) {
    inline fun <reified T : State> getState(id: StateId<T>): T {
        return stateManager.getState(id)
    }

    inline fun <reified T : State> getStateOrNull(id: StateId<T>): T? {
        return stateManager.getStateOrNull(id)
    }
}