package fr.delphes.features.twitch.endCredits

import fr.delphes.feature.NonEditableFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("end-credits")
class EndCreditsDescription : NonEditableFeatureDescription {
    override val editable = false
    override val id: String = UUID.randomUUID().toString()
}