import DynamicFormService from "@/common/dynamicForm/DynamicFormService";
import {FieldDuration} from "@/common/dynamicForm/field/duration/FieldDuration";
import {fieldFromJson} from "@/common/dynamicForm/field/Field.factory";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import {Duration} from "@/common/utils/Duration";
import {describe, expect, it} from "vitest";

describe("FieldDurationTest", () => {
    describe("Field factory", () => {
        it("build Duration field", () => {
            const json = `
            {
                "type": "DURATION",
                "fieldName": "fieldName",
                "description": "description",
                "value": "PT1H2M4S"
            }
        `

            const field = fieldFromJson(JSON.parse(json), new DynamicFormService(""))

            expect(field).to.toMatchObject(
                {
                    "description": "description",
                    "fieldName": "fieldName",
                    "initialValue": new Duration(1, 2, 4),
                    "actualValue": new Duration(1, 2, 4),
                }
            )
        })
    })
    describe("Field serialization", () => {
        it("serialize Duration field", () => {
            const field = new FieldDuration("description", "fieldName", new Duration(1, 2, 4));

            expect(field.buildJsonValue()).toStrictEqual(new FieldJsonValue("fieldName", "PT1H2M4S"))
        })
    })
})