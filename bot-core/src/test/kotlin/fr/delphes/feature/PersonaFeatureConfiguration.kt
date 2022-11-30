package fr.delphes.feature

enum class PersonaFeatureConfiguration(
    override val id: String,
    private val builderRuntime: () -> PersonaFeature?
) : ExperimentalFeatureConfiguration<PersonaFeature> {
    WORKING(PersonaFeature.WORKING.id, { PersonaFeature.WORKING }),
    WORKING_2(PersonaFeature.WORKING.id, { PersonaFeature.WORKING }),
    NOT_WORKING("configuration not working", { null }),
    FAILING("configuration failing", { throw RuntimeException("Something went wrong.") }),
    SERVE_NOT_WORKING_FEATURE(PersonaFeature.NOT_WORKING.id, { PersonaFeature.NOT_WORKING }),
    SERVE_FAILING_FEATURE(PersonaFeature.WITH_FAILURE.id, { PersonaFeature.WITH_FAILURE });

    override fun buildFeature(id: String): PersonaFeature? {
        return builderRuntime()
    }
}