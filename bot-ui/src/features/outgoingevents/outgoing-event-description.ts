import {FieldDescriptor} from "@/common/describable-form/field-descriptor";
import {fromJsonDescriptor} from "@/common/describable-form/field-descriptor.factory";
import {FormDescription} from "@/common/describable-form/form-description";
import {OutgoingEvent} from "@/features/outgoingevents/outgoing-event";

type jsonFormat = {
    type: string,
    descriptors: FieldDescriptor<any>[]
};

export class OutgoingEventDescription implements FormDescription<OutgoingEvent> {
    constructor(
        public type: string,
        public descriptors: FieldDescriptor<any>[]
    ) {
    }

    buildValue(): OutgoingEvent {
        return {
            type: this.type,
            ...this.descriptors
                .map(descriptor => descriptor.buildValue())
                .reduce((acc, value) => ({...acc, [value.fieldName]: value.value}), {})
        }
    }

    static fromJson(json: jsonFormat): OutgoingEventDescription {
        return new OutgoingEventDescription(
            json.type,
            json.descriptors.map((descriptor: any) => fromJsonDescriptor(descriptor))
        );
    }
}