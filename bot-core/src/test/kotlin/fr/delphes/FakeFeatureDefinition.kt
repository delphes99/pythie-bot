package fr.delphes

import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.state.State
import fr.delphes.state.StateProvider

class FakeFeatureDefinition(
    override val id: FeatureId = FeatureId("id"),
    private val runtime: FeatureRuntime,
) : FeatureDefinition {
    override fun buildRuntime(stateManager: StateProvider): FeatureRuntime {
        return runtime
    }

    override fun getSpecificStates(stateProvider: StateProvider) = emptyList<State>()
}