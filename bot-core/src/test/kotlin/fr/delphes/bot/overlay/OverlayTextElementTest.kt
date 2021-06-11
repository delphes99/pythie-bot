package fr.delphes.bot.overlay

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class OverlayTextElementSerializationTest {
    private val serializer = Json {
        ignoreUnknownKeys = true
        isLenient = false
        encodeDefaults = true
        coerceInputValues = true
    }

    @Test
    internal fun deserialize() {
        serializer.decodeFromString<OverlayElement>(
            """
                {
                  "type": "Text",
                  "id": "0ea18104-c775-4741-acba-5c0944b554bb",
                  "left": 273,
                  "top": 308,
                  "text": "my text"
                }
            """
        ).shouldBe(
            OverlayTextElement(
                "0ea18104-c775-4741-acba-5c0944b554bb",
                273,
                308,
                "my text"
            )
        )
    }

    @Test
    internal fun serialize() {
        serializer.encodeToString(
            OverlayTextElement(
                "0ea18104-c775-4741-acba-5c0944b554bb",
                273,
                308,
                "my text"
            )
        ).shouldEqualJson(
            """
                {
                  "id": "0ea18104-c775-4741-acba-5c0944b554bb",
                  "top": 308,
                  "left": 273,
                  "text": "my text"
                }
            """
        )
    }
}