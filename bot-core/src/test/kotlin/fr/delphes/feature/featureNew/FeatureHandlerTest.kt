package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class FeatureHandlerTest {
    @Nested
    inner class LoadConfiguration {
        @Test
        internal fun init() {
            val featureHandler = FeatureHandler()

            featureHandler.load(listOf())

            featureHandler.runtimes shouldBe emptyList()
            featureHandler.configurations shouldBe emptyList()
        }

        @Test
        internal fun `load a configuration`() {
            val featureHandler = FeatureHandler()

            val runtime = FeatureRuntime()
            val configuration = FeatureConfigurationTest("id", runtime)

            featureHandler.load(listOf(configuration))

            featureHandler.runtimes.shouldContainExactly(runtime)
            featureHandler.configurations.shouldContainExactly(configuration)
        }

        @Test
        internal fun `replace a configuration`() {
            val identifier = "id"
            val oldRuntime = FeatureRuntime()
            val configuration = FeatureConfigurationTest(
                identifier,
                oldRuntime
            )
            val featureHandler = FeatureHandler(configuration)

            val newRuntime = FeatureRuntime()
            val newConfiguration = FeatureConfigurationTest(identifier, newRuntime)

            featureHandler.load(listOf(newConfiguration))

            featureHandler.runtimes.shouldContainExactly(newRuntime)
            featureHandler.configurations.shouldContainExactly(newConfiguration)
        }
    }

    @Nested
    inner class HandleIncomingEvent {
        @Test
        internal fun `dispatch incoming event responses`() {
            val incomingEvent = mockk<IncomingEvent>()
            val outgoingEvent = mockk<OutgoingEvent>()

            val runtime = mockk<FeatureRuntime>()
            every { runtime.execute(incomingEvent) } returns listOf(outgoingEvent)

            val featureHandler = FeatureHandler(
                FeatureConfigurationTest("id", runtime)
            )

            val outgoingEvents = featureHandler.handleIncomingEvent(incomingEvent)

            outgoingEvents.shouldContainExactly(outgoingEvent)
        }
    }
}

private class FeatureConfigurationTest(
    override val identifier: String,
    private val runtimeToBuild: FeatureRuntime
) : FeatureConfiguration {
    override fun buildRuntime(): FeatureRuntime {
        return runtimeToBuild
    }
}