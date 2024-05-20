import {getColumnValue} from "@/common/designSystem/table/UiTableColumn.extract";
import {describe, expect, it} from "vitest";

describe("ColumnTransformation.getColumnValue", () => {
    it("return null if no value", () => {
        expect(getColumnValue(null, "field")).toStrictEqual(undefined);
    })
    it("return null if no corresponding field", () => {
        expect(getColumnValue({}, "field")).toStrictEqual(undefined);
    })
    it("return field value", () => {
        expect(getColumnValue({field: "someValue"}, "field")).toStrictEqual("someValue");
    })
    it("return recursive field value", () => {
        expect(getColumnValue({container: {innerField: {otherField: "someValue"}}}, "container.innerField.otherField")).toStrictEqual("someValue");
    })
    it("return recursive field value", () => {
        expect(getColumnValue({container: {innerField: {otherField: "someValue"}}}, "container.nonExisting")).toStrictEqual(undefined);
    })
});