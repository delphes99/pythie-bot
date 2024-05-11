import {DurationDescriptor} from "@/common/dynamicForm/field/duration/duration-descriptor";
import {FieldDescriptor} from "@/common/dynamicForm/field/field-descriptor";
import {FieldDescriptorType} from "@/common/dynamicForm/field/field-descriptor-type";
import {FormListDescriptor} from "@/common/dynamicForm/field/formList/form-list-descriptor";
import {MapDescriptor} from "@/common/dynamicForm/field/map/map-descriptor";
import {OutgoingEventsDescriptor} from "@/common/dynamicForm/field/outgoingEvents/outgoing-events-descriptor";
import {StringDescriptor} from "@/common/dynamicForm/field/string/string-descriptor";

export type DescriptorJsonType = {
    type: FieldDescriptorType;
    fieldName: string;
    [key: string]: any;
};

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