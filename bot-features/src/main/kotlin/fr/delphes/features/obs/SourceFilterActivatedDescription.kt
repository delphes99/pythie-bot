package fr.delphes.features.obs

import fr.delphes.feature.NonEditableFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("obs-source-filter-activated")
class SourceFilterActivatedDescription : NonEditableFeatureDescription {
    override val editable = false
    override val id: String = UUID.randomUUID().toString()
}