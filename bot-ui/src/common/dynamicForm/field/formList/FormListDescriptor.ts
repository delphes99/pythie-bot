import {DynamicFormFamily} from "@/common/dynamicForm/DynamicFormFamily";
import {FieldDescriptor} from "@/common/dynamicForm/field/FieldDescriptor";
import {DescriptorJsonType} from "@/common/dynamicForm/field/FieldDescriptor.factory";
import {FieldDescriptorType} from "@/common/dynamicForm/field/FieldDescriptorType";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import FieldFormListEditView from "@/common/dynamicForm/field/formList/FieldFormListEditView.vue";

export class FormListDescriptor implements FieldDescriptor<string> {
    static readonly type: FieldDescriptorType = FieldDescriptorType.FORM_LIST;

    constructor(
        readonly description: string,
        readonly fieldName: string,
        readonly formFamily: DynamicFormFamily,
        readonly initialValue: string,
        readonly actualValue: string = initialValue
    ) {
    }

    static fromJson(json: DescriptorJsonType): FormListDescriptor {
        if (json.type !== this.type) {
            throw new Error(`Cannot deserialize ${json.type} as FormListDescriptor`);
        }

        return new FormListDescriptor(
            json.description,
            json.fieldName,
            json.formFamily,
            JSON.stringify(json.value)
        );
    }

    buildValue() {
        return new FieldJsonValue(this.fieldName, JSON.parse(this.actualValue));
    }

    viewComponent() {
        return FieldFormListEditView
    }
}