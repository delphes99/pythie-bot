package fr.delphes.feature

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

class PersonaFeatureRuntime(
    override val feature: ExperimentalFeature<ExperimentalFeatureRuntime>
) : ExperimentalFeatureRuntime {
    override fun execute(incomingEvent: IncomingEvent): List<OutgoingEvent> {
        return emptyList()
    }
}
