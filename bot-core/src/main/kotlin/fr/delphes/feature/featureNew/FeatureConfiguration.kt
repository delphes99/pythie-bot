package fr.delphes.feature.featureNew

interface FeatureConfiguration {
    fun buildRuntime() : FeatureRuntime

    val identifier: FeatureIdentifier
}