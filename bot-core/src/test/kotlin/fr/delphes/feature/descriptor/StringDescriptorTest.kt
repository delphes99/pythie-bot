package fr.delphes.feature.descriptor

import fr.delphes.descriptor.item.ItemDescriptor
import fr.delphes.descriptor.item.StringDescriptor
import io.kotest.assertions.json.shouldEqualJson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class StringDescriptorTest {
    @Test
    internal fun serialize() {
        val item: ItemDescriptor = StringDescriptor("test", "value")

        Json.encodeToString(item).shouldEqualJson(
            """
            {
                "type": "description-string",
                "name": "test",
                "value": "value"
            }
            """
        )
    }

    @Test
    internal fun `serialize without value`() {
        val item: ItemDescriptor = StringDescriptor("test")

        Json.encodeToString(item).shouldEqualJson(
            """
            {
                "type": "description-string",
                "name": "test"
            }
            """
        )
    }
}