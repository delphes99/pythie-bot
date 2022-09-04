package fr.delphes.feature

enum class PersonnaFeatureConfiguration(
    override val id: String,
    private val builderRuntime: () -> PersonnaFeature?
) : ExperimentalFeatureConfiguration<PersonnaFeature> {
    WORKING(PersonnaFeature.WORKING.id, { PersonnaFeature.WORKING }),
    WORKING_2(PersonnaFeature.WORKING.id, { PersonnaFeature.WORKING }),
    NOT_WORKING("configuration not working", { null }),
    FAILING("configuration failing", { throw RuntimeException("Something went wrong.") }),
    SERVE_NOT_WORKING_FEATURE(PersonnaFeature.NOT_WORKING.id, { PersonnaFeature.NOT_WORKING }),
    SERVE_FAILING_FEATURE(PersonnaFeature.WITH_FAILURE.id, { PersonnaFeature.WITH_FAILURE });

    override fun buildFeature(id: String): PersonnaFeature? {
        return builderRuntime()
    }
}