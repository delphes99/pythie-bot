package fr.delphes.feature

import fr.delphes.FakeFeatureDefinition
import fr.delphes.FakeFeatureRuntime
import fr.delphes.FakeIncomingEvent
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.SimpleFeatureRuntime
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

internal class FeaturesManagerTest : ShouldSpec({
    context("handle incoming message") {
        should("build runtime when feature is registered") {
            val runtime = mockk<SimpleFeatureRuntime>()
            val featuresManager = FeaturesManager(
                mockk(),
                listOf(FakeFeatureDefinition(ID, runtime)),
                mockk()
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
                listOf(
                    FakeFeatureDefinition(ID, runtime1),
                    FakeFeatureDefinition(ID, runtime2)
                ),
                mockk()
            )

            featuresManager.handle(FakeIncomingEvent(), mockk())

            isRuntime1Called shouldBe true
            isRuntime2Called shouldBe true
        }
    }
}) {
    companion object {
        val ID = FeatureId("id")
    }
}