package fr.delphes.feature

import fr.delphes.descriptor.registry.DescriptorRegistry
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class FeaturesManagerTest {
    @Test
    internal fun `no descriptor if no configuration`() {
        runBlocking {
            val repository = TestRepository()
            val registry = DescriptorRegistry.of<ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>>()

            FeaturesManager(repository, registry, registry).getDescriptors().shouldBeEmpty()
        }
    }

    @Test
    internal fun `no descriptor if no mapper`() {
        runBlocking {
            val repository = TestRepository(PersonnaFeatureConfiguration.WORKING)
            val registry = DescriptorRegistry.of<ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>>()

            FeaturesManager(repository, registry, registry).getDescriptors().shouldBeEmpty()
        }
    }

    @Test
    internal fun `get descriptor`() {
        runBlocking {
            val repository = TestRepository(PersonnaFeatureConfiguration.WORKING)
            val registry = DescriptorRegistry.of(
                personnaFeatureMapper
            )

            FeaturesManager(repository, registry, registry).getDescriptors().shouldContain(personnaFeatureDescriptor)
        }
    }

    private class TestRepository(
        private val configurations: List<ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>>
    ) : ExperimentalFeatureConfigurationRepository {
        constructor(vararg configurations: ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>) : this(configurations.toList())

        override suspend fun getAll(): List<ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>> {
            return configurations
        }
    }
}