import {describe, expect, it} from "vitest";
import {FieldValue} from "@/common/describable-form/field-descriptor";
import {DurationDescriptor} from "@/common/describable-form/duration/duration-descriptor";
import {Duration} from "@/common/duration.utils";
import {FieldDescriptorType} from "@/common/describable-form/field-descriptor-type";

describe("Duration descriptor", () => {
    it("construct from JSON", () => {
        const descriptor = DurationDescriptor.fromJson({
            type: FieldDescriptorType.DURATION,
            fieldName: "fieldName",
            description: "description",
            value: "PT30S",
        })

        expect(descriptor).toStrictEqual(new DurationDescriptor(
            "description",
            "fieldName",
            new Duration(0, 0, 30)
        ))
    })
    it("build with actual value", () => {
        const descriptor = new DurationDescriptor(
            "description",
            "fieldName",
            new Duration(0, 0, 30)
        )
        descriptor.actualValue = new Duration(1, 2, 3)

        const value = descriptor.buildValue()

        expect(value).toStrictEqual(new FieldValue("fieldName", "PT1H2M3S"))
    })
});