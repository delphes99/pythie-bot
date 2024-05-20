import {FieldDuration} from "@/common/dynamicForm/field/duration/FieldDuration";
import {fieldFromJson} from "@/common/dynamicForm/field/Field.factory";
import {Duration} from "@/common/utils/Duration";
import {describe, expect, it} from "vitest";

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

        const field = fieldFromJson(JSON.parse(json));

        expect(field).toStrictEqual(new FieldDuration("description", "fieldName", new Duration(1, 2, 4)))
    })
})