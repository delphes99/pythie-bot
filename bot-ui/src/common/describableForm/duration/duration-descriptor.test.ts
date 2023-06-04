import {DurationDescriptor} from "@/common/describableForm/duration/duration-descriptor";
import {FieldValue} from "@/common/describableForm/field-descriptor";
import {Duration} from "@/common/utils/duration.utils";
import {describe, expect, it} from "vitest";

describe("Duration descriptor", () => {
    it("build with actual value", () => {
        const descriptor = new DurationDescriptor(
            "description",
            "fieldName",
            new Duration(1, 2, 3)
        )

        const value = descriptor.buildValue()

        expect(value).toStrictEqual(new FieldValue("fieldName", "PT1H2M3S"))
    })
    it("copy with new hours value", () => {
        const descriptor = new DurationDescriptor(
            "description",
            "fieldName",
            new Duration(1, 2, 3)
        )

        const value = descriptor.withHours(4).buildValue()

        expect(value).toStrictEqual(new FieldValue("fieldName", "PT4H2M3S"))
    })
    it("copy with new minutes value", () => {
        const descriptor = new DurationDescriptor(
            "description",
            "fieldName",
            new Duration(1, 2, 3)
        )

        const value = descriptor.withMinutes(4).buildValue()

        expect(value).toStrictEqual(new FieldValue("fieldName", "PT1H4M3S"))
    })
    it("copy with new seconds value", () => {
        const descriptor = new DurationDescriptor(
            "description",
            "fieldName",
            new Duration(1, 2, 3)
        )

        const value = descriptor.withSeconds(4).buildValue()

        expect(value).toStrictEqual(new FieldValue("fieldName", "PT1H2M4S"))
    })
});