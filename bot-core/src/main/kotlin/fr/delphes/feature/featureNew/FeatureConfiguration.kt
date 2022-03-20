package fr.delphes.feature.featureNew

interface FeatureConfiguration<STATE : FeatureState> {
    fun buildRuntime() : FeatureRuntime<STATE>

    fun toFeature(): Feature<STATE> {
        return Feature(this, buildRuntime())
    }

    val identifier: FeatureIdentifier
}