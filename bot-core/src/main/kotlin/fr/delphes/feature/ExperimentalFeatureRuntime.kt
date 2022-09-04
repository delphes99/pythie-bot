package fr.delphes.feature

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

interface ExperimentalFeatureRuntime {
    val feature : ExperimentalFeature<ExperimentalFeatureRuntime>

    fun execute(incomingEvent: IncomingEvent): List<OutgoingEvent>
}
