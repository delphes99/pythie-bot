package fr.delphes.rework.feature

import fr.delphes.state.StateManager

interface FeatureDefinition {
    val id: FeatureId
    fun buildRuntime(stateManager: StateManager): FeatureRuntime
}
