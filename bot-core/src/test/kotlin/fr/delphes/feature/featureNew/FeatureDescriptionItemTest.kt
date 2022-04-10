package fr.delphes.feature.featureNew

import io.kotest.assertions.json.shouldEqualJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class FeatureDescriptionItemTest {
    private val JSON = Json { }

    @Test
    internal fun `serialize string`() {
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

    @Test
    internal fun `serialize serializable item`() {
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

    @Serializable
    private class SerializableItem(val value: String)
}