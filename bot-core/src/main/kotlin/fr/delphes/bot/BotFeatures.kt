package fr.delphes.bot

import fr.delphes.bot.event.outgoing.OutgoingEventBuilderDescription
import fr.delphes.bot.event.outgoing.OutgoingEventBuilderType
import fr.delphes.feature.featureNew.FeatureConfiguration
import fr.delphes.feature.featureNew.FeatureIdentifier
import fr.delphes.feature.featureNew.FeatureType

class BotFeatures(
    private val features: Map<FeatureType, (FeatureIdentifier) -> FeatureConfiguration<*>>,
    private val outgoingEvents: Map<OutgoingEventBuilderType, OutgoingEventBuilderDescription>
) {
    fun build(id: FeatureIdentifier, type: FeatureType) : FeatureConfiguration<*>? {
        return features.filter { (key, _) -> key == type }.map { (_, value) -> value(id) }.firstOrNull()
    }

    fun getOutgoingEventsTypes(): Set<OutgoingEventBuilderType> {
        return outgoingEvents.keys
    }

    fun getOutgoingEventsDescriptionOf(type: OutgoingEventBuilderType): OutgoingEventBuilderDescription? {
        return outgoingEvents[type]
    }
}