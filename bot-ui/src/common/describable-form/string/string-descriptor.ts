import {FieldDescriptor, FieldValue} from "@/common/describable-form/field-descriptor";
import {FieldDescriptorType} from "@/common/describable-form/field-descriptor-type";
import {DescriptorJsonType} from "@/common/describable-form/field-descriptor.factory";
import FieldStringEditView from "@/common/describable-form/string/field-string-edit-view.vue";

export class StringDescriptor implements FieldDescriptor<string> {
    actualValue: string;
    type: FieldDescriptorType = FieldDescriptorType.STRING

    constructor(
        readonly description: string,
        readonly fieldName: string,
        readonly initialValue: string
    ) {
        this.actualValue = initialValue;
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