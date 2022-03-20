package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent

class IncomingEventFilters<STATE : FeatureState>(
    private val filters: List<IncomingEventFilter<STATE>> = emptyList()
) : FilterIncomingEvent<STATE> {
    constructor(vararg filters: IncomingEventFilter<STATE>) : this(listOf(*filters))

    override fun isApplicable(incomingEvent: IncomingEvent, state: STATE): Boolean {
        return filters.all { filter -> filter.isApplicable(incomingEvent, state) }
    }
}