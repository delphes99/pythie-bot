package fr.delphes.feature.featureNew

import kotlinx.serialization.json.Json

interface FeatureConfiguration<STATE : FeatureState> {
    fun buildRuntime() : FeatureRuntime<STATE>?

    fun toFeature(): Feature<STATE>? {
        val runtime = buildRuntime()
        return runtime?.let { Feature(this, runtime) }
    }

    fun description(serializer: Json): FeatureDescription

    val identifier: FeatureIdentifier
}