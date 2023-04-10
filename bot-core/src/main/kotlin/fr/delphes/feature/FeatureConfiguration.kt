package fr.delphes.feature

import fr.delphes.rework.feature.CustomFeature
import fr.delphes.rework.feature.FeatureId

interface FeatureConfiguration {
    val id: FeatureId
    fun buildFeature(): CustomFeature
}