import {DynamicFormFamily} from "@/common/dynamicForm/dynamic-form-family";
import {FieldDescriptor, FieldValue} from "@/common/dynamicForm/field/field-descriptor";
import {FieldDescriptorType} from "@/common/dynamicForm/field/field-descriptor-type";
import {DescriptorJsonType} from "@/common/dynamicForm/field/field-descriptor.factory";
import FieldFormListEditView from "@/common/dynamicForm/field/formList/field-form-list-edit-view.vue";

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
        return new FieldValue(this.fieldName, JSON.parse(this.actualValue));
    }

    viewComponent() {
        return FieldFormListEditView
    }
}