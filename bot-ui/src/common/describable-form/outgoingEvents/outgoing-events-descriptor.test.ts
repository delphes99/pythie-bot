import {FieldValue} from "@/common/describable-form/field-descriptor";
import {OutgoingEventsDescriptor} from "@/common/describable-form/outgoingEvents/outgoing-events-descriptor";
import {TestFieldDescriptor} from "@/common/describable-form/test-field-descriptor.util.test";
import {OutgoingEventDescription} from "@/features/outgoingevents/outgoing-event-description";
import {describe, expect, it} from "vitest";

describe("Outgoing events description", () => {
    it("build value should return empty array when no events", () => {
        const description = new OutgoingEventsDescriptor(
            "fieldName",
            "description",
            []
        );
        expect(description.buildValue()).toStrictEqual(new FieldValue("fieldName", []));
    })
    it("build value should return event payloads in json format", () => {
        const description = new OutgoingEventsDescriptor(
            "fieldName",
            "description",
            [
                new OutgoingEventDescription(
                    "event type",
                    [
                        new TestFieldDescriptor("fieldName", "description", "first value"),
                        new TestFieldDescriptor("anotherField", "bla bla", "second value"),
                    ]
                ),
                new OutgoingEventDescription(
                    "another event type",
                    [
                        new TestFieldDescriptor("fieldName", "description", "some value")
                    ]
                )
            ]
        );
        expect(description.buildValue()).toStrictEqual(
            new FieldValue("fieldName", [
                {
                    type: "event type",
                    fieldName: "first value",
                    anotherField: "second value",
                },
                {
                    type: "another event type",
                    fieldName: "some value",
                }
            ])
        );
    })
});

