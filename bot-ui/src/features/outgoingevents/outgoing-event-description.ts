import {FieldDescriptor} from "@/common/describable-form/field-descriptor";
import {fromJsonDescriptor} from "@/common/describable-form/field-descriptor.factory";
import {FormDescription} from "@/common/describable-form/form-description";
import {OutgoingEvent} from "@/features/outgoingevents/outgoing-event";
import {v4 as uuid} from "uuid";

type jsonFormat = {
    type: string,
    descriptors: FieldDescriptor<any>[]
};

export class OutgoingEventDescription implements FormDescription<OutgoingEvent> {
    readonly id: string = uuid();

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

    modifyDescriptor(modification: FieldDescriptor<any>) {
        return new OutgoingEventDescription(
            this.type,
            this.descriptors.map((descriptor) => {
                    if (descriptor.fieldName === modification.fieldName) {
                        return modification
                    }

                    return descriptor
                }
            )
        )
    }

    static fromJson(json: jsonFormat): OutgoingEventDescription {
        return new OutgoingEventDescription(
            json.type,
            json.descriptors.map((descriptor: any) => fromJsonDescriptor(descriptor))
        );
    }
}