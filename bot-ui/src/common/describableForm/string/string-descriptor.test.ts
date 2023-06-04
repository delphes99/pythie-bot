import {FieldValue} from "@/common/describableForm/field-descriptor";
import {FieldDescriptorType} from "@/common/describableForm/field-descriptor-type";
import {StringDescriptor} from "@/common/describableForm/string/string-descriptor";
import {describe, expect, it} from "vitest";

describe("String descriptor", () => {
    it("construct from JSON", () => {
        const descriptor = StringDescriptor.fromJson({
            type: FieldDescriptorType.STRING,
            fieldName: "fieldName",
            description: "description",
            value: "value",
        });

        expect(descriptor).toStrictEqual(
            new StringDescriptor(
                "description",
                "fieldName",
                "value"
            )
        )
    })
    it("build with actual value", () => {
        const descriptor = new StringDescriptor("description", "fieldName", "actualValue");

        const value = descriptor.buildValue();

        expect(value).toStrictEqual(new FieldValue("fieldName", "actualValue"));
    })
    it("copy with new value", () => {
        const descriptor = new StringDescriptor("description", "fieldName", "actualValue");

        const newDescriptor = descriptor.withValue("newValue").buildValue();

        expect(newDescriptor).toStrictEqual(new FieldValue("fieldName", "newValue"));
    });
});