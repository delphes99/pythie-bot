package fr.delphes.features.twitch.statistics

import fr.delphes.feature.NonEditableFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("statistics")
class StatisticsDescription : NonEditableFeatureDescription {
    override val editable = false
    override val id: String = UUID.randomUUID().toString()
}