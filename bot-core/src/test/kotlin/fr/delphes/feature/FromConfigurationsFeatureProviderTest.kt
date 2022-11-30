package fr.delphes.feature

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

class FromConfigurationsFeatureProviderTest : ShouldSpec({
    should("load editable feature from repository") {
        val featureConfigurationRepository = PersonaFeatureConfigurationRepository(PersonaFeatureConfiguration.WORKING)
        val manager = FromConfigurationsFeatureProvider(
            featureConfigurationRepository
        )

        manager.get().shouldContainExactlyInAnyOrder(PersonaFeature.WORKING)
    }

    should("load only editable configuration which can be transform into feature") {
        val featureConfigurationRepository = PersonaFeatureConfigurationRepository(PersonaFeatureConfiguration.WORKING, PersonaFeatureConfiguration.NOT_WORKING)
        val manager = FromConfigurationsFeatureProvider(
            featureConfigurationRepository
        )

        manager.get().shouldContainExactlyInAnyOrder(PersonaFeature.WORKING)
    }

    should("don't fail on build feature exception") {
        val featureConfigurationRepository = PersonaFeatureConfigurationRepository(PersonaFeatureConfiguration.WORKING, PersonaFeatureConfiguration.FAILING)
        val manager = FromConfigurationsFeatureProvider(
            featureConfigurationRepository
        )

        manager.get().shouldContainExactlyInAnyOrder(PersonaFeature.WORKING)
    }
})
