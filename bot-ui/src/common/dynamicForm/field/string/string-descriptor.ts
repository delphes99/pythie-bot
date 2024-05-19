import {FieldDescriptor, FieldValue} from "@/common/dynamicForm/field/field-descriptor";
import {FieldDescriptorType} from "@/common/dynamicForm/field/field-descriptor-type";
import {DescriptorJsonType} from "@/common/dynamicForm/field/field-descriptor.factory";
import StringDescriptorEditView from "@/common/dynamicForm/field/string/string-descriptor-edit-view.vue";

export class StringDescriptor implements FieldDescriptor<string> {
    static readonly type: FieldDescriptorType = FieldDescriptorType.STRING

    constructor(
        readonly description: string,
        readonly fieldName: string,
        readonly initialValue: string,
        readonly actualValue: string = initialValue
    ) {
    }

    withValue(newValue: string): StringDescriptor {
        return new StringDescriptor(
            this.description,
            this.fieldName,
            this.initialValue,
            newValue
        );
    }

    static fromJson(json: DescriptorJsonType): StringDescriptor {
        if (json.type !== this.type) {
            throw new Error(`Cannot deserialize ${json.type} as StringDescriptor`);
        }

        return new StringDescriptor(
            json.description,
            json.fieldName,
            json.value
        );
    }

    buildValue() {
        return new FieldValue(this.fieldName, this.actualValue);
    }

    viewComponent() {
        return StringDescriptorEditView;
    }
}