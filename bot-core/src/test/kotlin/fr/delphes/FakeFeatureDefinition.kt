package fr.delphes

import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.state.StateManager

class FakeFeatureDefinition(
    override val id: FeatureId = FeatureId("id"),
    private val runtime: FeatureRuntime
) : FeatureDefinition {
    override fun buildRuntime(stateManager: StateManager): FeatureRuntime {
        return runtime
    }
}