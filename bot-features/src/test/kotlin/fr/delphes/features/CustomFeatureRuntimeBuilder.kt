package fr.delphes.features

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.rework.feature.CustomFeature
import fr.delphes.state.State
import fr.delphes.state.StateManager
import fr.delphes.state.state.ClockState
import fr.delphes.test.TestClock
import fr.delphes.test.personna.NOW
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class CustomFeatureRuntimeBuilder(
    private val feature: CustomFeature
) {
    private var fixedNow: LocalDateTime = NOW
    @PublishedApi
    internal val stateManager = StateManager()

    fun atFixedTime(now: LocalDateTime): CustomFeatureRuntimeBuilder {
        fixedNow = now
        return this
    }

    inline fun <reified U : State> withState(state: U): CustomFeatureRuntimeBuilder {
        stateManager.withState(state)
        return this
    }

    suspend fun hasReceived(event: IncomingEvent) {
        val runtime = feature.buildRuntime(
            stateManager
                .withState(ClockState(TestClock(fixedNow)))
        )
        runtime.handleIncomingEvent(event, mockk { every { featuresManager.stateManager } returns stateManager })
    }
}

fun CustomFeature.atFixedTime(now: LocalDateTime): CustomFeatureRuntimeBuilder {
    return CustomFeatureRuntimeBuilder(this).atFixedTime(now)
}

suspend fun CustomFeature.hasReceived(event: IncomingEvent) {
    CustomFeatureRuntimeBuilder(this).hasReceived(event)
}