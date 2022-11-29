package fr.delphes.feature

import fr.delphes.FakeFeatureDefinition
import fr.delphes.FakeFeatureRuntime
import fr.delphes.FakeIncomingEvent
import fr.delphes.descriptor.registry.DescriptorRegistry
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.SimpleFeatureRuntime
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

internal class FeaturesManagerTest : ShouldSpec({
    context("handle incoming message") {
        val ID = FeatureId("id")
        should("build runtime when feature is registered") {
            val runtime = mockk<SimpleFeatureRuntime>()
            val featuresManager = FeaturesManager(
                mockk(),
                mockk(),
                mockk(),
                mockk(),
                listOf(FakeFeatureDefinition(ID, runtime))
            )

            featuresManager.getRuntime(ID) shouldBe runtime
        }

        should("deliver message to all runtimes") {
            var isRuntime1Called = false
            val runtime1 = FakeFeatureRuntime { _, _ -> isRuntime1Called = true }
            var isRuntime2Called = false
            val runtime2 = FakeFeatureRuntime { _, _ -> isRuntime2Called = true }
            val featuresManager = FeaturesManager(
                mockk(),
                mockk(),
                mockk(),
                mockk(),
                listOf(
                    FakeFeatureDefinition(ID, runtime1),
                    FakeFeatureDefinition(ID, runtime2)
                )
            )

            featuresManager.handle(FakeIncomingEvent(), mockk())

            isRuntime1Called shouldBe true
            isRuntime2Called shouldBe true
        }
    }

    //TODO delete ?
    context("descritor") {
        should("no descriptor if no configuration") {
            runTest {
                val repository = TestRepository()
                val registry = DescriptorRegistry.of<ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>>()

                FeaturesManager(repository, registry, registry).getDescriptors().shouldBeEmpty()
            }
        }

        should("no descriptor if no mapper") {
            runTest {
                val repository = TestRepository(PersonnaFeatureConfiguration.WORKING)
                val registry = DescriptorRegistry.of<ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>>()

                FeaturesManager(repository, registry, registry).getDescriptors().shouldBeEmpty()
            }
        }

        should("get descriptor") {
            runTest {
                val repository = TestRepository(PersonnaFeatureConfiguration.WORKING)
                val registry = DescriptorRegistry.of(
                    personnaFeatureMapper
                )

                FeaturesManager(repository, registry, registry).getDescriptors().shouldContain(personnaFeatureDescriptor)
            }
        }
    }
})

private class TestRepository(
    private val configurations: List<ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>>
) : ExperimentalFeatureConfigurationRepository {
    constructor(vararg configurations: ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>) : this(configurations.toList())

    override suspend fun getAll(): List<ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>> {
        return configurations
    }
}