package fr.delphes.feature

enum class PersonaFeature(
    override val id: String,
    private val builderRuntime: () -> PersonaFeatureRuntime?
) : ExperimentalFeature<PersonaFeatureRuntime> {
    WORKING("working", { PersonaFeatureRuntime(WORKING)}),
    WORKING_2("working2", { PersonaFeatureRuntime(WORKING_2)}),
    NOT_WORKING("not working", {  null }),
    WITH_FAILURE("with failure", { throw RuntimeException("Something went wrong.") });

    override fun buildRuntime(): PersonaFeatureRuntime? {
        return builderRuntime()
    }
}