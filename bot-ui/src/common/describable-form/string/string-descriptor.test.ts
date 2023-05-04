import {FieldValue} from "@/common/describable-form/field-descriptor";
import {FieldDescriptorType} from "@/common/describable-form/field-descriptor-type";
import {StringDescriptor} from "@/common/describable-form/string/string-descriptor";
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
        const descriptor = new StringDescriptor("description", "fieldName", "value");
        descriptor.actualValue = "actualValue";

        const value = descriptor.buildValue();
        expect(value).toStrictEqual(new FieldValue("fieldName", "actualValue"));
    })
});