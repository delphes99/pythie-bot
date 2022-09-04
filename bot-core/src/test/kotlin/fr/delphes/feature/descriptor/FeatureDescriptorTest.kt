package fr.delphes.feature.descriptor

import fr.delphes.descriptor.Descriptor
import fr.delphes.descriptor.DescriptorType
import io.kotest.assertions.json.shouldEqualJson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class FeatureDescriptorTest {
    private val serializer = Json {
        ignoreUnknownKeys = true
        isLenient = false
        encodeDefaults = true
        coerceInputValues = true
    }

    @Test
    internal fun serialize() {
        val encodeToString = serializer.encodeToString(Descriptor(DescriptorType("type-name")))
        encodeToString.shouldEqualJson("""{
            "type": "type-name",
            "items": []
        }"""
        )
    }
}