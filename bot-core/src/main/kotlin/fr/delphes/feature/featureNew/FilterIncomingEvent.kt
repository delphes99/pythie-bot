package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent

interface FilterIncomingEvent {
    fun isApplicable(incomingEvent: IncomingEvent): Boolean
}