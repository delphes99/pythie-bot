package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent

interface FeatureRuntime<STATE : FeatureState> {
    var state: STATE

    fun execute(incomingEvent: IncomingEvent): List<OutgoingEvent>
}