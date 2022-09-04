package fr.delphes.feature

class NonEditableFeatureProvider(
    private val nonEditableFeatures: List<ExperimentalFeature<ExperimentalFeatureRuntime>> = emptyList()
) : FeaturesProvider {
    override suspend fun get(): List<ExperimentalFeature<ExperimentalFeatureRuntime>> {
        return nonEditableFeatures
    }
}