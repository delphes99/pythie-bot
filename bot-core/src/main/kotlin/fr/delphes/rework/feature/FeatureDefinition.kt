package fr.delphes.rework.feature

import fr.delphes.state.StateManager
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock

interface FeatureDefinition {
    val id: FeatureId
    fun buildRuntime(stateManager: StateManager, clock: Clock = SystemClock): FeatureRuntime
}
