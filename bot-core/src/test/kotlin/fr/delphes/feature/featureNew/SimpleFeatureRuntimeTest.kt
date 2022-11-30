package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

internal class SimpleFeatureRuntimeTest : ShouldSpec({
    should("execute responses when filters are applicable") {
        val runtime = SimpleFeatureRuntime(
            IncomingEventFilters(FILTER_APPLICABLE),
            INITIAL_STATE
        ) { _, _ -> RuntimeResult.NewState(NEW_STATE, listOf(OUTGOING_EVENT)) }

        runtime.execute(INCOMING_EVENT).shouldContainExactly(OUTGOING_EVENT)
    }

    should("should not execute responses when filters are not applicable") {
        val runtime = SimpleFeatureRuntime(
            IncomingEventFilters(FILTER_NOT_APPLICABLE),
            INITIAL_STATE
        ) { _, _ -> fail("should not execute") }

        runtime.execute(INCOMING_EVENT).shouldBeEmpty()
    }

    should("should save new state when filters are applicable") {
        val runtime = SimpleFeatureRuntime(
            IncomingEventFilters(FILTER_APPLICABLE),
            INITIAL_STATE
        ) { _, _ -> RuntimeResult.NewState(NEW_STATE, listOf(OUTGOING_EVENT)) }

        runtime.execute(INCOMING_EVENT)

        runtime.state shouldBe NEW_STATE
    }

    context("no state") {
        should("execute responses") {
            val runtime = SimpleFeatureRuntime.noState(
                IncomingEventFilters(listOf(IncomingEventFilter { _, _ -> true }))
            ) { listOf(OUTGOING_EVENT) }

            runtime.execute(INCOMING_EVENT).shouldContainExactly(OUTGOING_EVENT)
        }

        should("should not execute responses if filters are off") {
            val runtime = SimpleFeatureRuntime.noState(
                IncomingEventFilters(listOf(IncomingEventFilter { _, _ -> false }))
            ) { fail("should not execute") }

            runtime.execute(INCOMING_EVENT).shouldBeEmpty()
        }
    }
}) {
    companion object {
        private val INCOMING_EVENT = mockk<IncomingEvent>()
        private val OUTGOING_EVENT = mockk<OutgoingEvent>()

        val FILTER_APPLICABLE = listOf<IncomingEventFilter<StateTest>>(IncomingEventFilter { _, _ -> true })
        val FILTER_NOT_APPLICABLE = listOf<IncomingEventFilter<StateTest>>(IncomingEventFilter { _, _ -> false })

        private val INITIAL_STATE = StateTest("initial state")
        private val NEW_STATE = StateTest("new state")

        data class StateTest(val value: String) : FeatureState
    }
}