import {DynamicForm} from "@/common/dynamicForm/DynamicForm";
import {Field} from "@/common/dynamicForm/field/Field";
import {FieldJsonType} from "@/common/dynamicForm/field/Field.factory";
import FieldFormListEditView from "@/common/dynamicForm/field/formList/FieldFormListEditView.vue";
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