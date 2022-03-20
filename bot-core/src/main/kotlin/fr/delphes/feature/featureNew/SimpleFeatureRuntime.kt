package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

class SimpleFeatureRuntime<STATE : FeatureState>(
    private val filters: IncomingEventFilters<STATE> = IncomingEventFilters(),
    override var state: STATE,
    private val responses: (STATE, IncomingEvent) -> Pair<STATE, List<OutgoingEvent>> = { oldState, _ -> oldState to emptyList() }
) : FilterIncomingEvent<STATE> by filters, FeatureRuntime<STATE> {

    override fun execute(incomingEvent: IncomingEvent): List<OutgoingEvent> {
        val (newState, outgoingEvents) = execute(incomingEvent, state)

        if (state != newState) {
            state = newState
        }

        return outgoingEvents
    }

    fun execute(incomingEvent: IncomingEvent, state: STATE): Pair<STATE, List<OutgoingEvent>> {
        return if (filters.isApplicable(incomingEvent, state)) {
            responses(state, incomingEvent)
        } else {
            state to emptyList()
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
            ) { _, incomingEvent -> NoState to responses(incomingEvent) }
        }
    }
}