package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

class FeatureRuntime(
    private val filters: IncomingEventFilters = IncomingEventFilters(),
    private val responses: (IncomingEvent) -> List<OutgoingEvent> = { emptyList() }
): FilterIncomingEvent by filters {
    fun execute(incomingEvent: IncomingEvent) : List<OutgoingEvent> {
        return if(filters.isApplicable(incomingEvent)) {
            responses(incomingEvent)
        } else {
            emptyList()
        }
    }
}