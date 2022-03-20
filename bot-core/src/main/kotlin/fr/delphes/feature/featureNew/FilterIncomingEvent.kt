package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent

interface FilterIncomingEvent<STATE : FeatureState> {
    fun isApplicable(incomingEvent: IncomingEvent, state: STATE): Boolean
}