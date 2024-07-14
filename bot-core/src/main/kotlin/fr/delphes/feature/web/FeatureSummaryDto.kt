package fr.delphes.feature.web

import fr.delphes.dynamicForm.DynamicFormType
import fr.delphes.rework.feature.FeatureId
import kotlinx.serialization.Serializable

@Serializable
class FeatureSummaryDto(
    val id: FeatureId,
    val type: DynamicFormType,
)