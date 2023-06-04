import {FieldDescriptor, FieldValue} from "@/common/describableForm/field-descriptor";
import {FieldDescriptorType} from "@/common/describableForm/field-descriptor-type";
import {DescriptorJsonType} from "@/common/describableForm/field-descriptor.factory";
import FieldStringEditView from "@/common/describableForm/string/field-string-edit-view.vue";

export class StringDescriptor implements FieldDescriptor<string> {
    type: FieldDescriptorType = FieldDescriptorType.STRING

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
        if (json.type !== FieldDescriptorType.STRING) {
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
        return FieldStringEditView
    }
}