package fr.delphes.feature.featureNew

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class FeatureDescriptionItem(
    val type: FeatureDescriptionItemType,
    val name: String,
    val currentValue: JsonElement
) {
    companion object {
        inline fun <reified T> build(
            type: FeatureDescriptionItemType,
            name: String,
            currentValue: T,
            serializer: Json
        ): FeatureDescriptionItem {
            return FeatureDescriptionItem(
                type,
                name,
                serializer.encodeToJsonElement(currentValue)
            )
        }
    }
}