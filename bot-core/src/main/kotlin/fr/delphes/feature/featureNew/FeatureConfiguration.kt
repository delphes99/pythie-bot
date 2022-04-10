package fr.delphes.feature.featureNew

import kotlinx.serialization.json.Json

interface FeatureConfiguration<STATE : FeatureState> {
    fun buildRuntime() : FeatureRuntime<STATE>

    fun toFeature(): Feature<STATE> {
        return Feature(this, buildRuntime())
    }

    fun description(serializer: Json): FeatureDescription

    val identifier: FeatureIdentifier
}