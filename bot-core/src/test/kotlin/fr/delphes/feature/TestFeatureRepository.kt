package fr.delphes.feature

class TestFeatureRepository(private val features: List<ExperimentalFeature<out ExperimentalFeatureRuntime>>) : FeaturesProvider {
    constructor(vararg features: ExperimentalFeature<out ExperimentalFeatureRuntime>) : this(features.toList())

    override suspend fun get(): List<ExperimentalFeature<out ExperimentalFeatureRuntime>> {
        return features
    }
}