package fr.delphes.feature.descriptor

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.dynamicForm.descriptor.DurationFieldDescriptor
import fr.delphes.dynamicForm.descriptor.FieldDescriptor
import fr.delphes.dynamicForm.descriptor.OutgoingEventsFieldDescriptor
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.serializer
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import java.time.Duration

class OutgoingEventsFeatureDescriptorTest : ShouldSpec({
    should("serialization with no events") {
        val descriptor = OutgoingEventsFieldDescriptor(
            fieldName = "fieldName",
            description = "description",
            value = emptyList()
        )

        val payload = serializer.encodeToString<FieldDescriptor>(descriptor)

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
        val descriptor = OutgoingEventsFieldDescriptor(
            fieldName = "fieldName",
            description = "description",
            value = listOf(DESCRIPTION)
        )

        val payload = serializer.encodeToString<FieldDescriptor>(descriptor)

        payload shouldEqualJson EXPECTED_PAYLOAD
    }

    should("serialization with events from builder") {
        val descriptor = OutgoingEventsFieldDescriptor.fromBuilders(
            fieldName = "fieldName",
            description = "description",
            builders = listOf(
                mockk<OutgoingEventBuilder> {
                    coEvery { description() } returns DESCRIPTION
                }
            )
        )

        val payload = serializer.encodeToString<FieldDescriptor>(descriptor)

        payload shouldEqualJson EXPECTED_PAYLOAD
    }
}) {
    companion object {
        private val DESCRIPTION = OutgoingEventBuilderDescription(
            type = OutgoingEventType("SOME_EVENT"),
            listOf(
                StringFieldDescriptor(
                    fieldName = "stringValue",
                    description = "description of stringValue",
                    value = "stringValue"
                ),
                DurationFieldDescriptor(
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