package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.ObsConnector
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
sealed interface ObsOutgoingEvent : OutgoingEvent {
    suspend fun executeOnObs(connector: ObsConnector)
}