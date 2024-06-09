import {Field} from "@/common/dynamicForm/field/Field";
import {FieldJsonType} from "@/common/dynamicForm/field/Field.factory";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import FieldStringEditView from "@/common/dynamicForm/field/string/FieldStringEditView.vue";
import {Component} from "vue";


export class FieldString implements Field<string> {
    static fromJson(json: FieldJsonType) {
        return new FieldString(
            json.description,
            json.fieldName,
            json.value
        );
    }

    readonly id: string = crypto.randomUUID()

    constructor(
        public readonly description: string,
        public readonly fieldName: string,
        public readonly initialValue: string,
        public actualValue: string = initialValue
    ) {
    }

    viewComponent(): Component {
        return FieldStringEditView;
    }

    buildJsonValue(): FieldJsonValue {
        return new FieldJsonValue(this.fieldName, this.actualValue)
    }
}