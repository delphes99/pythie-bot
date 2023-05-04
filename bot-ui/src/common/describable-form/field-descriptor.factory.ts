import {FieldDescriptorType} from "@/common/describable-form/field-descriptor-type";
import {FieldDescriptor} from "@/common/describable-form/field-descriptor";
import {StringDescriptor} from "@/common/describable-form/string/string-descriptor";
import {OutgoingEventsDescriptor} from "@/common/describable-form/outgoingEvents/outgoing-events-descriptor";
import {DurationDescriptor} from "@/common/describable-form/duration/duration-descriptor";

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