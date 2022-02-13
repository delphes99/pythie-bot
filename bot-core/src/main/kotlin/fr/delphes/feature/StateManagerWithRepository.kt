package fr.delphes.feature

import kotlinx.coroutines.runBlocking

class StateManagerWithRepository<T : State>(
    private val stateRepository: StateRepository<T>
) : WithStateManager<T> {
    override var state: T = runBlocking { stateRepository.load() }

    override suspend fun newState(newState : T) {
        state = newState
        stateRepository.save(newState)
    }
}