import {DurationDescriptor} from "@/common/describableForm/duration/duration-descriptor";
import {FieldDescriptor} from "@/common/describableForm/field-descriptor";
import {FieldDescriptorType} from "@/common/describableForm/field-descriptor-type";
import {OutgoingEventsDescriptor} from "@/common/describableForm/outgoingEvents/outgoing-events-descriptor";
import {StringDescriptor} from "@/common/describableForm/string/string-descriptor";

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
    }
}