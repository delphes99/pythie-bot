import DynamicFormService from "@/common/dynamicForm/DynamicFormService";
import {fieldFromJson} from "@/common/dynamicForm/field/Field.factory";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import {FieldString} from "@/common/dynamicForm/field/string/FieldString";
import {describe, expect, it} from "vitest";

describe("FieldStringTest", () => {
    describe("Field factory", () => {
        it("build String field", () => {
            const json = `
            {
                "type": "STRING",
                "fieldName": "fieldName",
                "description": "description",
                "value": "value"
            }`

            const field = fieldFromJson(JSON.parse(json), new DynamicFormService(""))

            expect(field).to.toMatchObject({
                "description": "description",
                "fieldName": "fieldName",
                "initialValue": "value",
                "actualValue": "value",
            })
        })
    })
    describe("Field serialization", () => {
        it("serialize String field", () => {
            const field = new FieldString("description", "fieldName", "value");

            expect(field.buildJsonValue()).toStrictEqual(new FieldJsonValue("fieldName", "value"))
        })
    })
})