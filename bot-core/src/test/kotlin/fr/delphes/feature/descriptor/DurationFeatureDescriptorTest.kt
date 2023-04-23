package fr.delphes.feature.descriptor

import fr.delphes.serializer
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import kotlinx.serialization.encodeToString
import java.time.Duration

class DurationFeatureDescriptorTest : ShouldSpec({
    should("serialization") {
        val descriptor = DurationFeatureDescriptor(
            fieldName = "fieldName",
            description = "description",
            value = Duration.ofHours(2).plusMinutes(30).plusSeconds(15)
        )

        val payload = serializer.encodeToString<FeatureDescriptor>(descriptor)

        payload shouldEqualJson """
            {
                "type": "DURATION",
                "fieldName": "fieldName",
                "description": "description",
                "value": "PT2H30M15S"
            }
        """
    }
})