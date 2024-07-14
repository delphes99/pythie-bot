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

        should("deliver message to all runtimes") {
            val compiledFeature1Id = FeatureId("compiled1")
            val compiledFeature1Runtime = FakeFeatureRuntime()
            val compiledFeature2Id = FeatureId("compiled2")
            val compiledFeature2Runtime = FakeFeatureRuntime()

            val configurableFeature1Id = FeatureId("configurable1")
            val configurableFeature1Runtime = FakeFeatureRuntime()
            val configurableFeature2Id = FeatureId("configurable2")
            val configurableFeature2Runtime = FakeFeatureRuntime()

            val featureConfigurationRepository = TestFeatureConfigurationRepository(
                fr.delphes.rework.feature.Feature(
                    configurableFeature1Id,
                    FakeFeatureDefinition(
                        configurableFeature1Id,
                        configurableFeature1Runtime
                    )
                ),
                fr.delphes.rework.feature.Feature(
                    configurableFeature2Id,
                    FakeFeatureDefinition(
                        configurableFeature2Id,
                        configurableFeature2Runtime
                    )
                ),
            )

            val featuresManager = FeaturesManager(
                mockk {
                    every { readOnly } returns StateProvider(this)
                },
                listOf(
                    FakeFeatureDefinition(compiledFeature1Id, compiledFeature1Runtime),
                    FakeFeatureDefinition(compiledFeature2Id, compiledFeature2Runtime)
                ),
                featureConfigurationRepository
            )

            featuresManager.handle(
                IncomingEventWrapper(FakeIncomingEvent(), LocalDateTime.of(2020, 1, 2, 12, 0)),
                mockk()
            )

            configurableFeature1Runtime.isHandleIncomingEventCalled shouldBe true
            configurableFeature2Runtime.isHandleIncomingEventCalled shouldBe true
            compiledFeature1Runtime.isHandleIncomingEventCalled shouldBe true
            compiledFeature2Runtime.isHandleIncomingEventCalled shouldBe true
        }
    }
}) {
    companion object {
        val ID = FeatureId("id")
    }
}