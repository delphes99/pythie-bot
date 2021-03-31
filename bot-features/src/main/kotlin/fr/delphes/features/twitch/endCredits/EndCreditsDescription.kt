package fr.delphes.features.twitch.endCredits

import fr.delphes.feature.FeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("end-credits")
class EndCreditsDescription : FeatureDescription {
    override val id: String = UUID.randomUUID().toString()
}