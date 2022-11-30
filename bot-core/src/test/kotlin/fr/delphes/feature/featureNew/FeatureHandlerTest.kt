package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.Json

class FeatureHandlerTest : ShouldSpec({
    context("Load configuration") {
        should("init") {
            val featureHandler = FeatureHandler()

            featureHandler.load(listOf())

            featureHandler.runtimes shouldBe emptyList()
            featureHandler.configurations shouldBe emptyList()
        }

        should("load a configuration") {
            val featureHandler = FeatureHandler()

            val runtime = SimpleFeatureRuntime.noState()
            val configuration = FeatureConfigurationTest("id", runtime)

            featureHandler.load(listOf(configuration))

            featureHandler.runtimes.shouldContainExactly(runtime)
            featureHandler.configurations.shouldContainExactly(configuration)
        }

        should("replace a configuration") {
            val identifier = "id"
            val oldRuntime = SimpleFeatureRuntime.noState()
            val configuration = FeatureConfigurationTest(
                identifier,
                oldRuntime
            )
            val featureHandler = FeatureHandler(configuration)

            val newRuntime = SimpleFeatureRuntime.noState()
            val newConfiguration = FeatureConfigurationTest(identifier, newRuntime)

            featureHandler.load(listOf(newConfiguration))

            featureHandler.runtimes.shouldContainExactly(newRuntime)
            featureHandler.configurations.shouldContainExactly(newConfiguration)
        }
    }
    context("handle incoming event") {
        should("dispatch incoming event responses") {
            val incomingEvent = mockk<IncomingEvent>()
            val outgoingEvent = mockk<OutgoingEvent>()

            val runtime = mockk<SimpleFeatureRuntime<NoState>>()
            every { runtime.execute(incomingEvent) } returns listOf(outgoingEvent)

            val featureHandler = FeatureHandler(
                FeatureConfigurationTest("id", runtime)
            )

            val outgoingEvents = featureHandler.handleIncomingEvent(incomingEvent)

            outgoingEvents.shouldContainExactly(outgoingEvent)
        }
    }
}) {
    companion object {


        private class FeatureConfigurationTest(
            override val identifier: String,
            private val runtimeToBuild: SimpleFeatureRuntime<NoState>
        ) : FeatureConfiguration<NoState> {
            override fun buildRuntime(): SimpleFeatureRuntime<NoState> {
                return runtimeToBuild
            }

            override fun description(serializer: Json): FeatureDescription {
                error("no serializer for test")
            }
        }
    }
}