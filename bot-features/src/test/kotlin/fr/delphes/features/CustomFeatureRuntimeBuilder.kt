package fr.delphes.features

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.state.StateManager
import fr.delphes.state.state.ClockState
import fr.delphes.test.TestClock
import fr.delphes.test.personna.NOW
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class CustomFeatureRuntimeBuilder(
    private val feature: FeatureDefinition,
) {
    private var fixedNow: LocalDateTime = NOW

    @PublishedApi
    internal val stateManager = StateManager()

    fun atFixedTime(now: LocalDateTime) {
        fixedNow = now
    }

    inline fun <reified U : State> withState(state: U) {
        stateManager.withState(state)
    }

    inline fun <reified U : State> withMockedState(stateId: StateId<U>, configureMock: U.() -> Unit = {}) {
        val state = mockk<U> {
            every { id } returns stateId
        }.also(configureMock)
        stateManager.put(state)
    }

    suspend fun hasReceived(event: IncomingEvent) {
        val runtime = feature.buildRuntime(
            stateManager
                .withState(ClockState(TestClock(fixedNow)))
        )
        runtime.handleIncomingEvent(event, mockk { every { featuresManager.stateManager } returns stateManager })
    }
}

fun FeatureDefinition.testRuntime(builder: CustomFeatureRuntimeBuilder.() -> Unit = {}): CustomFeatureRuntimeBuilder {
    return CustomFeatureRuntimeBuilder(this).apply(builder)
}