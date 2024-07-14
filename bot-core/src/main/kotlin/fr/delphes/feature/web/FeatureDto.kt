package fr.delphes.feature.web

import fr.delphes.dynamicForm.DynamicFormDescription
import fr.delphes.rework.feature.FeatureId
import kotlinx.serialization.Serializable

@Serializable
class FeatureDto(
    val id: FeatureId,
    val definition: DynamicFormDescription,
)