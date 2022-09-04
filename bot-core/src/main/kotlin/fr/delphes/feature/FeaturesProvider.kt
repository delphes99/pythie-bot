package fr.delphes.feature

interface FeaturesProvider {
    suspend fun get(): List<ExperimentalFeature<ExperimentalFeatureRuntime>>
}