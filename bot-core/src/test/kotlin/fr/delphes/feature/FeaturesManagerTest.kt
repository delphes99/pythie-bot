package fr.delphes.feature

import fr.delphes.FakeFeatureDefinition
import fr.delphes.FakeFeatureRuntime
import fr.delphes.FakeIncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.SimpleFeatureRuntime
import fr.delphes.state.StateProvider
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

internal class FeaturesManagerTest : ShouldSpec({
    context("handle incoming message") {
        should("build runtime when compiled feature is registered") {
            val runtime = mockk<SimpleFeatureRuntime>()
            val featuresManager = FeaturesManager(
                mockk {
                    every { readOnly } returns StateProvider(this)
                },
                listOf(FakeFeatureDefinition(ID, runtime)),
                mockk(relaxed = true)
            )

            featuresManager.getRuntime(ID) shouldBe runtime
        }

        //TODO test with feature configuration
        should("deliver message to all runtimes") {
            val runtimeForCompiledFeature = FakeFeatureRuntime()
            val runtimeForAnotherCompiledFeature = FakeFeatureRuntime()
            val runtimeForConfigurableFeature = FakeFeatureRuntime()
            val runtimeForAnotherConfigurableFeature = FakeFeatureRuntime()

            val featureConfigurationRepository = mockk<FeatureConfigurationRepository> {
                coEvery { load() } returns listOf(
                    mockk {
                        every { buildFeature(any()) } returns mockk {
                            every { buildRuntime(any()) } returns runtimeForConfigurableFeature
                            every { getSpecificStates(any()) } returns emptyList()
                        }
                    },
                    mockk {
                        every { buildFeature(any()) } returns mockk {
                            every { buildRuntime(any()) } returns runtimeForAnotherConfigurableFeature
                            every { getSpecificStates(any()) } returns emptyList()
                        }
                    }
                )
            }

            val featuresManager = FeaturesManager(
                mockk {
                    every { readOnly } returns StateProvider(this)
                },
                listOf(
                    FakeFeatureDefinition(ID, runtimeForCompiledFeature),
                    FakeFeatureDefinition(ID, runtimeForAnotherCompiledFeature)
                ),
                featureConfigurationRepository
            )

            featuresManager.handle(
                IncomingEventWrapper(FakeIncomingEvent(), LocalDateTime.of(2020, 1, 2, 12, 0)),
                mockk()
            )

            runtimeForCompiledFeature.isHandleIncomingEventCalled shouldBe true
            runtimeForAnotherCompiledFeature.isHandleIncomingEventCalled shouldBe true
            runtimeForConfigurableFeature.isHandleIncomingEventCalled shouldBe true
            runtimeForAnotherConfigurableFeature.isHandleIncomingEventCalled shouldBe true
        }
    }
}) {
    companion object {
        val ID = FeatureId("id")
    }
}