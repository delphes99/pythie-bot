package fr.delphes.features.overlay

import fr.delphes.feature.FeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("overlay")
class OverlayDescription : FeatureDescription {
    override val id: String = UUID.randomUUID().toString()
}