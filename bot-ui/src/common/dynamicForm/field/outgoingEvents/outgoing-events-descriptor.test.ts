import {FieldValue} from "@/common/dynamicForm/field/field-descriptor";
import {OutgoingEventsDescriptor} from "@/common/dynamicForm/field/outgoingEvents/outgoing-events-descriptor";
import {TestFieldDescriptor} from "@/common/dynamicForm/test-field-descriptor.test.utils";
import {OutgoingEventDescription} from "@/features/outgoingevents/outgoing-event-description";
import {describe, expect, it} from "vitest";

describe("Outgoing events description", () => {
    const event1 = new OutgoingEventDescription(
        "event type",
        [
            new TestFieldDescriptor("fieldName", "first value", "description"),
            new TestFieldDescriptor("anotherField", "second value", "bla bla"),
        ]
    );
    const event2 = new OutgoingEventDescription(
        "another event type",
        [
            new TestFieldDescriptor("fieldName", "some value", "description")
        ]
    );
    const descriptor = new OutgoingEventsDescriptor(
        "fieldName",
        "description",
        [
            event1,
            event2
        ]
    );

    it("build value should return event payloads in json format", () => {
        expect(descriptor.buildValue()).toStrictEqual(
            new FieldValue(
                "fieldName",
                [
                    {
                        type: "event type",
                        fieldName: "first value",
                        anotherField: "second value",
                    },
                    {
                        type: "another event type",
                        fieldName: "some value",
                    }
                ]
            )
        );
    })
    it("delete event", () => {
        const newDescriptor = descriptor.deleteEvent(event1);
        expect(newDescriptor.buildValue()).toStrictEqual(
            new FieldValue(
                "fieldName",
                [
                    {
                        type: "another event type",
                        fieldName: "some value",
                    }
                ]
            )
        );
    });
});

