import {DynamicForm} from "@/common/dynamicForm/DynamicForm";
import DynamicFormService from "@/common/dynamicForm/DynamicFormService";
import {fieldFromJson} from "@/common/dynamicForm/field/Field.factory";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import {FieldFormList} from "@/common/dynamicForm/field/formList/FieldFormList";
import {FieldString} from "@/common/dynamicForm/field/string/FieldString";
import {describe, expect, it} from "vitest";

describe("FieldFormListTest", () => {
    describe("Field factory", () => {
        it("build Form list field with no existing form", () => {
            const json = `
            {
                "type": "FORM_LIST",
                "fieldName": "fieldName",
                "formFamily": "formFamily",
                "description": "description",
                "value": []
            }`

            const field = fieldFromJson(JSON.parse(json), new DynamicFormService(""))

            expect(field).to.toMatchObject({
                "description": "description",
                "fieldName": "fieldName",
                "formFamily": "formFamily",
                "initialValue": [],
                "actualValue": [],
            })
        })
    })
    describe("Field serialization", () => {
        it("serialize empty list", () => {
            const field = new FieldFormList("description", "fieldName", "formFamily", []);

            expect(field.buildJsonValue()).toStrictEqual(new FieldJsonValue("fieldName", []))
        })
        it("serialize subform (without id)", () => {
            const subForm = new DynamicForm(
                "subFormFamily",
                [
                    new FieldString("description", "fieldName", "value", "new value")
                ]
            )
            const field = new FieldFormList("description", "fieldName", "formFamily", [subForm]);

            expect(field.buildJsonValue()).toStrictEqual(new FieldJsonValue("fieldName", [
                {
                    "type": "subFormFamily",
                    "fieldName": "new value",
                }
            ]))
        })
    })
})