package fr.delphes.bot.event.outgoing

import fr.delphes.feature.featureNew.FeatureDescriptionItem
import kotlinx.serialization.Serializable

@Serializable
data class OutgoingEventBuilderDescription(
    val type: OutgoingEventBuilderType,
    val items: List<FeatureDescriptionItem>
)
