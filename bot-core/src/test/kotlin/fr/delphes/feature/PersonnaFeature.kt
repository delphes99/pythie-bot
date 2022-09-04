package fr.delphes.feature

enum class PersonnaFeature(
    override val id: String,
    private val builderRuntime: () -> PersonnaFeatureRuntime?
) : ExperimentalFeature<PersonnaFeatureRuntime> {
    WORKING("working", { PersonnaFeatureRuntime(WORKING)}),
    WORKING_2("working2", { PersonnaFeatureRuntime(WORKING_2)}),
    NOT_WORKING("not working", {  null }),
    WITH_FAILURE("with failure", { throw RuntimeException("Something went wrong.") });

    override fun buildRuntime(): PersonnaFeatureRuntime? {
        return builderRuntime()
    }
}