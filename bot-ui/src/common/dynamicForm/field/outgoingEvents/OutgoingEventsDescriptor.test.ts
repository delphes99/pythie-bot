import {FieldValue} from "@/common/dynamicForm/field/FieldDescriptor";
import {OutgoingEventsDescriptor} from "@/common/dynamicForm/field/outgoingEvents/OutgoingEventsDescriptor";
import {TestFieldDescriptor} from "@/common/dynamicForm/TestFieldDescriptor.test.utils";
import {OutgoingEventDescription} from "@/features/outgoingevents/OutgoingEventDescription";
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

