package fr.delphes.dynamicForm.descriptor

import fr.delphes.serializer
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import kotlinx.serialization.encodeToString

class StringFieldDescriptorTest : ShouldSpec({
    should("serialization") {
        val descriptor = StringFieldDescriptor(
            fieldName = "fieldName",
            description = "description",
            value = "value"
        )

        val payload = serializer.encodeToString<FieldDescriptor>(descriptor)

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