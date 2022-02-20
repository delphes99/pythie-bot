package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent

fun interface IncomingEventFilter {
    fun isApplicable(event: IncomingEvent): Boolean
}