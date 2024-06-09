import {DurationDescriptor} from "@/common/dynamicForm/field/duration/DurationDescriptor";
import {FieldDescriptor} from "@/common/dynamicForm/field/FieldDescriptor";
import {FieldDescriptorType} from "@/common/dynamicForm/field/FieldDescriptorType";
import {FormListDescriptor} from "@/common/dynamicForm/field/formList/FormListDescriptor";
import {MapDescriptor} from "@/common/dynamicForm/field/map/MapDescriptor";
import {OutgoingEventsDescriptor} from "@/common/dynamicForm/field/outgoingEvents/OutgoingEventsDescriptor";
import {StringDescriptor} from "@/common/dynamicForm/field/string/StringDescriptor";

export type DescriptorJsonType = {
    type: FieldDescriptorType;
    fieldName: string;
    [key: string]: any;
};

//TODO: Remove this
export function fromJsonDescriptor(descriptor: DescriptorJsonType): FieldDescriptor<any> {
    switch (descriptor.type) {
        case FieldDescriptorType.STRING:
            return StringDescriptor.fromJson(descriptor);
        case FieldDescriptorType.DURATION:
            return DurationDescriptor.fromJson(descriptor);
        case FieldDescriptorType.OUTGOING_EVENTS:
            return OutgoingEventsDescriptor.fromJson(descriptor);
        case FieldDescriptorType.MAP:
            return MapDescriptor.fromJson(descriptor);
        case FieldDescriptorType.FORM_LIST:
            return FormListDescriptor.fromJson(descriptor);
    }
}