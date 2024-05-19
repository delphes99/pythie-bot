import {Field} from "@/common/dynamicForm/field/field";
import {FieldJsonType} from "@/common/dynamicForm/field/field.factory";
import FieldStringEditView from "@/common/dynamicForm/field/string/field-string-edit-view.vue";
import {Component} from "vue";


export class FieldString implements Field<string> {
    constructor(
        public readonly description: string,
        public readonly fieldName: string,
        public readonly initialValue: string,
        public readonly actualValue: string = initialValue
    ) {
    }

    viewComponent(): Component {
        return FieldStringEditView;
    }

    static fromJson(json: FieldJsonType) {
        return new FieldString(
            json.description,
            json.fieldName,
            json.value
        );
    }
}