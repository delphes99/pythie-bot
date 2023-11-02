package fr.delphes.feature

import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.state.StateProvider

interface FeatureConfiguration {
    val id: FeatureId
    fun buildFeature(stateProvider: StateProvider): FeatureDefinition
    suspend fun description(): FeatureDescription
}