package fr.delphes.feature

import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId

interface FeatureConfiguration {
    val id: FeatureId
    fun buildFeature(): FeatureDefinition
    fun description(): FeatureDescription
}