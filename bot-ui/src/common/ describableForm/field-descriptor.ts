import {FieldDescriptorType} from "@/common/ describableForm/field-descriptor-type";
import FieldStringEditView from "@/common/ describableForm/descriptorView/Field-string-edit-view.vue";
import FieldDurationEditView from "@/common/ describableForm/descriptorView/Field-duration-edit-view.vue";

export class FieldDescriptor {
    fieldName: string
    description: string
    type: FieldDescriptorType
    value: string

    constructor(fieldName: string, description: string, type: FieldDescriptorType, value: string) {
        this.fieldName = fieldName;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    viewComponent(): any {
        switch (this.type) {
            case FieldDescriptorType.STRING:
                return FieldStringEditView
            case FieldDescriptorType.DURATION:
                return FieldDurationEditView
        }
    }
}