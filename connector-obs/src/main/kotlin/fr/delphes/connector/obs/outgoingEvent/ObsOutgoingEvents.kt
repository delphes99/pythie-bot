package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.ObsConnector

sealed interface ObsOutgoingEvent : OutgoingEvent {
    suspend fun executeOnObs(connector: ObsConnector)
}