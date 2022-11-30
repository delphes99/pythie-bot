package fr.delphes.feature

class PersonaFeatureConfigurationRepository(
    private val configurations: List<PersonaFeatureConfiguration>
) : ExperimentalFeatureConfigurationRepository {
    constructor(vararg configurations: PersonaFeatureConfiguration) : this(configurations.toList())

    override suspend fun getAll() = configurations
}