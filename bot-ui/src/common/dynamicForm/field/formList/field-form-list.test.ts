import {fieldFromJson} from "@/common/dynamicForm/field/field.factory";
import {FieldFormList} from "@/common/dynamicForm/field/formList/field-form-list";
import {describe, expect, it} from "vitest";

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

        const field = fieldFromJson(JSON.parse(json))

        expect(field).toStrictEqual(new FieldFormList("description", "fieldName", "formFamily", []));
    })
})