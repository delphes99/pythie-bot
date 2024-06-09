import {DynamicForm} from "@/common/dynamicForm/DynamicForm";
import {FieldString} from "@/common/dynamicForm/field/string/FieldString";
import {describe, expect, it} from "vitest";

describe("DynamicFormTest", () => {
    it("should pass", () => {
        const form = new DynamicForm(
            "formFamily",
            [
                new FieldString("description", "fieldName", "initial value", "new value"),
                new FieldString("description", "anotherFieldName", "another initial value", "another new value")
            ],
            "formId"
        )

        expect(form.buildJson()).toStrictEqual({
            "id": "formId",
            "type": "formFamily",
            "fieldName": "new value",
            "anotherFieldName": "another new value",
        })
    })
})