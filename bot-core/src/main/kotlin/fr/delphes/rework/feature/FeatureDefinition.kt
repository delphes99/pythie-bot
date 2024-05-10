package fr.delphes.rework.feature

import fr.delphes.annotation.dynamicForm.DynamicFormParent
import fr.delphes.state.State
import fr.delphes.state.StateProvider

@DynamicFormParent("feature")
interface FeatureDefinition {
    val id: FeatureId

    fun buildRuntime(stateManager: StateProvider): FeatureRuntime

    fun getSpecificStates(stateProvider: StateProvider): List<State>
}
