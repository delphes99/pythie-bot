package fr.delphes.feature

class PersonnaFeatureConfigurationRepository(
    private val configurations: List<PersonnaFeatureConfiguration>
) : ExperimentalFeatureConfigurationRepository {
    constructor(vararg configurations: PersonnaFeatureConfiguration) : this(configurations.toList())

    override suspend fun getAll() = configurations
}