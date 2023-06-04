import {TestFieldDescriptor} from "@/common/describableForm/test-field-descriptor.test.utils";
import {OutgoingEventDescription} from "@/features/outgoingevents/outgoing-event-description";
import {describe, expect, it} from "vitest";

describe("Outgoing events description", () => {
    it("build value should return type in json format", () => {
        const description = new OutgoingEventDescription(
            "type",
            []
        );
        expect(description.buildValue()).toStrictEqual({
            type: "type",
        });
    })
    it("build value should return field values in json format", () => {
        const description = new OutgoingEventDescription(
            "type",
            [
                new TestFieldDescriptor("fieldName", "first value", "description"),
                new TestFieldDescriptor("anotherField", "second value", "bla bla"),
            ]
        );
        expect(description.buildValue()).toStrictEqual({
            type: "type",
            fieldName: "first value",
            anotherField: "second value",
        });
    })
});

