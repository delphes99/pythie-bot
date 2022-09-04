package fr.delphes.feature

interface ExperimentalFeatureConfiguration<FEATURE : ExperimentalFeature<*>> {
    val id: String

    fun buildFeature(): FEATURE? {
        return buildFeature(id)
    }

    //TODO how to force id in the feature ??
    fun buildFeature(id: String): FEATURE?
}
