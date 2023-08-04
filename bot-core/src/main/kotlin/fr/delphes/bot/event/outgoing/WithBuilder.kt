package fr.delphes.bot.event.outgoing

import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.feature.descriptor.FeatureDescriptor

interface WithBuilder {
    val type: OutgoingEventType
    val builderDefinition: OutgoingEventBuilderDefinition

    fun buildDefinition(
        newBuilderProvider: () -> LegacyOutgoingEventBuilder,
    ) = OutgoingEventBuilderDefinition(
        type = this.type,
        newBuilderProvider = newBuilderProvider
    )

    fun buildDescription(
        vararg descriptors: FeatureDescriptor,
    ) = OutgoingEventBuilderDescription(
        type = this.builderDefinition.type,
        descriptors = descriptors.toList()
    )
}