package fr.delphes.features.overlay

import fr.delphes.feature.NonEditableFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("overlay")
class OverlayDescription : NonEditableFeatureDescription {
    override val editable = false
    override val id: String = UUID.randomUUID().toString()
}