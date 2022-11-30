package fr.delphes.feature.featureNew

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FeatureDescriptionItemTest : ShouldSpec({
    should("serialize string") {
        val featureDescriptionItem = FeatureDescriptionItem.build(
            FeatureDescriptionItemType.STRING,
            "toto",
            "Valeur",
            JSON
        )

        JSON.encodeToString(featureDescriptionItem) shouldEqualJson """
            {
                "type":"STRING",
                "name":"toto",
                "currentValue":"Valeur"
            }
        """.trimIndent()
    }

    should("serialize serializable item") {
        val featureDescriptionItem = FeatureDescriptionItem.build(
            FeatureDescriptionItemType.STRING,
            "toto",
            SerializableItem("value"),
            JSON
        )

        JSON.encodeToString(featureDescriptionItem) shouldEqualJson """
            {
                "type":"STRING",
                "name":"toto",
                "currentValue": {
                    "value": "value"
                }
            }
        """.trimIndent()
    }
}) {
    companion object {
        private val JSON = Json {
            prettyPrint = true
        }

        @Serializable
        private class SerializableItem(val value: String)
    }
}