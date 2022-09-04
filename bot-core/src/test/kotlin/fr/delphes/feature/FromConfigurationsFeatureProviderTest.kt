package fr.delphes.feature

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class FromConfigurationsFeatureProviderTest {
    @Test
    internal fun `load editable feature from repository`() {
        runBlocking {
            val featureConfigurationRepository = PersonnaFeatureConfigurationRepository(PersonnaFeatureConfiguration.WORKING)
            val manager = FromConfigurationsFeatureProvider(
                featureConfigurationRepository
            )

            manager.get().shouldContainExactlyInAnyOrder(PersonnaFeature.WORKING)
        }
    }

    @Test
    internal fun `load only editable configuration which can be transform into feature`() {
        runBlocking {
            val featureConfigurationRepository = PersonnaFeatureConfigurationRepository(PersonnaFeatureConfiguration.WORKING, PersonnaFeatureConfiguration.NOT_WORKING)
            val manager = FromConfigurationsFeatureProvider(
                featureConfigurationRepository
            )

            manager.get().shouldContainExactlyInAnyOrder(PersonnaFeature.WORKING)
        }
    }

    @Test
    internal fun `don't fail on build feature exception`() {
        runBlocking {
            val featureConfigurationRepository = PersonnaFeatureConfigurationRepository(PersonnaFeatureConfiguration.WORKING, PersonnaFeatureConfiguration.FAILING)
            val manager = FromConfigurationsFeatureProvider(
                featureConfigurationRepository
            )

            manager.get().shouldContainExactlyInAnyOrder(PersonnaFeature.WORKING)
        }
    }
}
