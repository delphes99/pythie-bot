package fr.delphes

import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.state.StateManager
import fr.delphes.utils.time.Clock

class FakeFeatureDefinition(
    override val id: FeatureId = FeatureId("id"),
    private val runtime: FeatureRuntime
) : FeatureDefinition {
    override fun buildRuntime(stateManager: StateManager, clock: Clock): FeatureRuntime {
        return runtime
    }
}