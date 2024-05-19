import {DynamicForm} from "@/common/dynamicForm/dynamic-form";
import {Field} from "@/common/dynamicForm/field/field";
import {FieldJsonType} from "@/common/dynamicForm/field/field.factory";
import FieldFormListEditView from "@/common/dynamicForm/field/formList/field-form-list-edit-view.vue";
import {Component} from "vue";


export class FieldFormList implements Field<DynamicForm[]> {
    constructor(
        public readonly description: string,
        public readonly fieldName: string,
        public readonly formFamily: string,
        public readonly initialValue: DynamicForm[],
        public readonly actualValue: DynamicForm[] = initialValue
    ) {
    }

    viewComponent(): Component {
        return FieldFormListEditView;
    }

    static fromJson(json: FieldJsonType) {
        return new FieldFormList(
            json.description,
            json.fieldName,
            json.formFamily,
            json.value
        );
    }
}