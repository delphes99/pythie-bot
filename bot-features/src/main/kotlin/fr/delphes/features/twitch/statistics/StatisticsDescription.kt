package fr.delphes.features.twitch.statistics

import fr.delphes.feature.FeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("statics")
class StatisticsDescription : FeatureDescription {
    override val id: String = UUID.randomUUID().toString()
}