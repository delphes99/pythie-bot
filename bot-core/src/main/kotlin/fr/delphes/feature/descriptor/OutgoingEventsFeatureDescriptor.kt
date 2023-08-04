package fr.delphes.feature.descriptor

import fr.delphes.bot.event.outgoing.LegacyOutgoingEventBuilder
import fr.delphes.feature.OutgoingEventBuilderDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("OUTGOING_EVENTS")
@Serializable
data class OutgoingEventsFeatureDescriptor(
    override val fieldName: String,
    override val description: String,
    override val value: List<OutgoingEventBuilderDescription> = emptyList(),
) : FeatureDescriptor() {
    companion object {
        fun fromBuilders(
            fieldName: String,
            description: String,
            builders: List<LegacyOutgoingEventBuilder>,
        ) = OutgoingEventsFeatureDescriptor(
            fieldName = fieldName,
            description = description,
            value = builders.map { it.description() },
        )
    }
}