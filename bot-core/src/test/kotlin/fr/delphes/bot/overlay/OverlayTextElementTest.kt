package fr.delphes.bot.overlay

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class OverlayTextElementSerializationTest: ShouldSpec({
    val serializer = Json {
        ignoreUnknownKeys = true
        isLenient = false
        encodeDefaults = true
        coerceInputValues = true
    }

    val json = """
                {
                  "general": {
                    "id": "b634f8b3-a437-44cb-a6fe-daa339c245a0",
                    "left": 514,
                    "top": 58,
                    "sortOrder": 0
                  },
                  "properties": {
                    "type": "Text",
                    "text": "my text",
                    "color": "#24B0A5",
                    "font": "Roboto",
                    "fontSize": "40"
                  }
                }
            """

    val obj: OverlayElement<OverlayElementProperties> = OverlayElement(
        OverlayElementGeneralProperties(
            id = "b634f8b3-a437-44cb-a6fe-daa339c245a0",
            left = 514,
            top = 58,
            sortOrder = 0
        ),
        OverlayTextElement(
            text = "my text",
            color = "#24B0A5",
            font = "Roboto",
            fontSize = "40"
        )
    )
    should("deserialize") {
        serializer.decodeFromString<OverlayElement<OverlayTextElement>>(
            json
        ).shouldBe(
            obj
        )
    }

    should("serialize") {
        serializer.encodeToString(
            obj
        ).shouldEqualJson(
            json
        )
    }
})