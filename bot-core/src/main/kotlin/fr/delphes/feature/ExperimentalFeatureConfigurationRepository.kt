package fr.delphes.feature

interface ExperimentalFeatureConfigurationRepository {
    suspend fun getAll(): List<ExperimentalFeatureConfiguration<out ExperimentalFeature<ExperimentalFeatureRuntime>>>
}
