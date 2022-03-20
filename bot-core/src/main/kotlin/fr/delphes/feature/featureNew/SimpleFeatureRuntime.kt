package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

class SimpleFeatureRuntime<STATE : FeatureState>(
    private val filters: IncomingEventFilters<STATE> = IncomingEventFilters(),
    override var state: STATE,
    private val responses: (STATE, IncomingEvent) -> RuntimeResult<STATE> = { _, _ -> RuntimeResult.SameState() }
) : FilterIncomingEvent<STATE> by filters, FeatureRuntime<STATE> {

    override fun execute(incomingEvent: IncomingEvent): List<OutgoingEvent> {
        val result = execute(incomingEvent, state)

        if (result is RuntimeResult.NewState) {
            state = result.state
        }

        return result.outgoingEvents
    }

    fun execute(incomingEvent: IncomingEvent, state: STATE): RuntimeResult<STATE> {
        return if (filters.isApplicable(incomingEvent, state)) {
            responses(state, incomingEvent)
        } else {
            RuntimeResult.SameState()
        }
    }

    companion object {
        fun noState(
            filters: IncomingEventFilters<NoState> = IncomingEventFilters(),
            responses: (IncomingEvent) -> List<OutgoingEvent> = { emptyList() }
        ): SimpleFeatureRuntime<NoState> {
            return SimpleFeatureRuntime(
                filters,
                NoState
            ) { _, incomingEvent -> RuntimeResult.SameState(responses(incomingEvent)) }
        }
    }
}

sealed class RuntimeResult<STATE : FeatureState>(
    val outgoingEvents: List<OutgoingEvent>
) {
    class NewState<STATE : FeatureState>(
        val state: STATE,
        outgoingEvents: List<OutgoingEvent> = emptyList()
    ) : RuntimeResult<STATE>(outgoingEvents)

    class SameState<STATE : FeatureState>(
        outgoingEvents: List<OutgoingEvent> = emptyList()
    ) : RuntimeResult<STATE>(outgoingEvents)
}