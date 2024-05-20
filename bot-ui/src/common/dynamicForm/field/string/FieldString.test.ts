import {fieldFromJson} from "@/common/dynamicForm/field/Field.factory";
import {FieldString} from "@/common/dynamicForm/field/string/FieldString";
import {describe, expect, it} from "vitest";

describe("Field factory", () => {
    it("build String field", () => {
        const json = `
            {
                "type": "STRING",
                "fieldName": "fieldName",
                "description": "description",
                "value": "value"
            }`

        const field = fieldFromJson(JSON.parse(json))

        expect(field).toStrictEqual(new FieldString("description", "fieldName", "value"));
    })
})