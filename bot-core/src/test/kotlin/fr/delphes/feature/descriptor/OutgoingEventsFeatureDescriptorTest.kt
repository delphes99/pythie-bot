package fr.delphes.feature.descriptor

import fr.delphes.bot.event.outgoing.LegacyOutgoingEventBuilder
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.serializer
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import java.time.Duration

class OutgoingEventsFeatureDescriptorTest : ShouldSpec({
    should("serialization with no events") {
        val descriptor = OutgoingEventsFeatureDescriptor(
            fieldName = "fieldName",
            description = "description",
            value = emptyList()
        )

        val payload = serializer.encodeToString<FeatureDescriptor>(descriptor)

        payload shouldEqualJson """
            {
                "type": "OUTGOING_EVENTS",
                "fieldName": "fieldName",
                "description": "description",
                "value": []
            }
        """
    }

    should("serialization with events") {
        val descriptor = OutgoingEventsFeatureDescriptor(
            fieldName = "fieldName",
            description = "description",
            value = listOf(DESCRIPTION)
        )

        val payload = serializer.encodeToString<FeatureDescriptor>(descriptor)

        payload shouldEqualJson EXPECTED_PAYLOAD
    }

    should("serialization with events from builder") {
        val descriptor = OutgoingEventsFeatureDescriptor.fromBuilders(
            fieldName = "fieldName",
            description = "description",
            builders = listOf(
                mockk<LegacyOutgoingEventBuilder> {
                    every { description() } returns DESCRIPTION
                }
            )
        )

        val payload = serializer.encodeToString<FeatureDescriptor>(descriptor)

        payload shouldEqualJson EXPECTED_PAYLOAD
    }
}) {
    companion object {
        private val DESCRIPTION = OutgoingEventBuilderDescription(
            type = OutgoingEventType("SOME_EVENT"),
            listOf(
                StringFeatureDescriptor(
                    fieldName = "stringValue",
                    description = "description of stringValue",
                    value = "stringValue"
                ),
                DurationFeatureDescriptor(
                    fieldName = "durationValue",
                    description = "description of durationValue",
                    value = Duration.ofSeconds(30)
                )
            )
        )

        private const val EXPECTED_PAYLOAD = """
            {
              "type": "OUTGOING_EVENTS",
              "fieldName": "fieldName",
              "description": "description",
              "value": [
                {
                    "type": "SOME_EVENT",
                    "descriptors": [
                        {
                            "type": "STRING",
                            "fieldName": "stringValue",
                            "description": "description of stringValue",
                            "value": "stringValue"
                        },
                        {
                            "type": "DURATION",
                            "fieldName": "durationValue",
                            "description": "description of durationValue",
                            "value": "PT30S"
                        }
                    ]
                }
              ]
            }
        """
    }
}