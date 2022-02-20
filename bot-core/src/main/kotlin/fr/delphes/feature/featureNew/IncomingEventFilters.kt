package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent

class IncomingEventFilters(
    private val filters: List<IncomingEventFilter> = emptyList()
) : FilterIncomingEvent {
    constructor(vararg filters: IncomingEventFilter) : this(listOf(*filters))

    override fun isApplicable(incomingEvent: IncomingEvent): Boolean {
        return filters.all { filter -> filter.isApplicable(incomingEvent) };
    }
}