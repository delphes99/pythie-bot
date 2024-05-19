import {FieldDuration} from "@/common/dynamicForm/field/duration/field-duration";
import {Field} from "@/common/dynamicForm/field/field";
import {FieldDescriptorType} from "@/common/dynamicForm/field/field-descriptor-type";
import {FieldFormList} from "@/common/dynamicForm/field/formList/field-form-list";
import {FieldString} from "@/common/dynamicForm/field/string/field-string";

export type FieldJsonType = {
    type: FieldDescriptorType;
    fieldName: string;
    value: any;
    [key: string]: any;
};

export function fieldFromJson(json: FieldJsonType): Field<any> {
    switch (json.type) {
        case FieldDescriptorType.STRING:
            return FieldString.fromJson(json)
        case FieldDescriptorType.DURATION:
            return FieldDuration.fromJson(json)
        case FieldDescriptorType.OUTGOING_EVENTS:
            throw new Error("Outgoing events are not supported")
        case FieldDescriptorType.MAP:
            throw new Error("Map is not supported")
        case FieldDescriptorType.FORM_LIST:
            return FieldFormList.fromJson(json)
    }
}