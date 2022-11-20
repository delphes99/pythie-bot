package fr.delphes.rework.feature

interface FeatureDefinition {
    val id: FeatureId
    fun buildRuntime(): FeatureRuntime
}
