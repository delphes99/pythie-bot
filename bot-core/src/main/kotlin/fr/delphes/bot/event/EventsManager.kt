package fr.delphes.bot.event

import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.bot.event.outgoing.OutgoingEventRegistry
import fr.delphes.feature.OutgoingEventType

class EventsManager(
    private val connectorInitializers: List<ConnectorInitializer>,
) {
    fun allGoingEventTypes(): List<OutgoingEventType> {
        return connectorInitializers
            .map(ConnectorInitializer::outgoingEventRegistry)
            .flatMap(OutgoingEventRegistry::types)
    }

    fun getNewOutgoingEvent(type: OutgoingEventType): OutgoingEventBuilder? {
        return connectorInitializers
            .map(ConnectorInitializer::outgoingEventRegistry)
            .firstNotNullOfOrNull { it.getRegistryEntry(type) }
            ?.buildNewInstance()
    }
}