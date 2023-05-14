package fr.delphes.feature

import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.serializer
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import kotlinx.serialization.encodeToString

class OutgoingEventBuilderDescriptionTest : ShouldSpec({
    should("serialize") {
        val builder = OutgoingEventBuilderDescription(
            "someEvent",
            StringFeatureDescriptor(
                fieldName = "someField",
                description = "someDescription",
                value = "someValue"
            )
        )

        val json = serializer.encodeToString(builder)

        json shouldEqualJson """
            {
              "type": "someEvent",
              "descriptors": [
                {
                  "type": "STRING",
                  "fieldName": "someField",
                  "description": "someDescription",
                  "value": "someValue"
                }
              ]
            }
      """
    }
})