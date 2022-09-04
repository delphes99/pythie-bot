package fr.delphes.feature

interface ExperimentalFeature<out RUNTIME : ExperimentalFeatureRuntime> {
    val id: String

    fun buildRuntime(): RUNTIME?
}