package fr.delphes.feature.featureNew

import fr.delphes.bot.event.incoming.IncomingEvent

fun interface IncomingEventFilter<STATE: FeatureState> {
    fun isApplicable(event: IncomingEvent, state: STATE): Boolean
}