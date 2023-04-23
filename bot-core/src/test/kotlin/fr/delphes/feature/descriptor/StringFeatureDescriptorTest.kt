package fr.delphes.feature.descriptor

import fr.delphes.serializer
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import kotlinx.serialization.encodeToString

class StringFeatureDescriptorTest : ShouldSpec({
    should("serialization") {
        val descriptor = StringFeatureDescriptor(
            fieldName = "fieldName",
            description = "description",
            value = "value"
        )

        val payload = serializer.encodeToString<FeatureDescriptor>(descriptor)

        payload shouldEqualJson """
            {
                "type": "STRING",
                "fieldName": "fieldName",
                "description": "description",
                "value": "value"
            }
        """
    }
})