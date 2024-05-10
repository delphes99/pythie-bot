import {DurationDescriptor} from "@/common/dynamicForm/duration/duration-descriptor";
import {FieldDescriptor} from "@/common/dynamicForm/field-descriptor";
import {FieldDescriptorType} from "@/common/dynamicForm/field-descriptor-type";
import {FormListDescriptor} from "@/common/dynamicForm/formList/form-list-descriptor";
import {MapDescriptor} from "@/common/dynamicForm/map/map-descriptor";
import {OutgoingEventsDescriptor} from "@/common/dynamicForm/outgoingEvents/outgoing-events-descriptor";
import {StringDescriptor} from "@/common/dynamicForm/string/string-descriptor";

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