package fr.delphes.feature.featureNew

data class Feature<STATE: FeatureState>(
    val configuration: FeatureConfiguration<STATE>,
    val runtime: FeatureRuntime<STATE>
)