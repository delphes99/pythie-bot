import {FieldDescriptor} from "@/common/dynamicForm/field/FieldDescriptor";
import {fromJsonDescriptor} from "@/common/dynamicForm/field/FieldDescriptor.factory";
import {FormDescription} from "@/common/dynamicForm/FormDescription";
import {OutgoingEvent} from "@/features/outgoingevents/OutgoingEvent";

type jsonFormat = {
    type: string,
    descriptors: FieldDescriptor<any>[]
};

export class OutgoingEventDescription implements FormDescription<OutgoingEvent> {
    constructor(
        public type: string,
        public descriptors: FieldDescriptor<any>[],
        readonly id: string = crypto.randomUUID(),
    ) {
    }

    buildValue(): OutgoingEvent {
        return {
            type: this.type,
            ...this.descriptors
                .map(descriptor => descriptor.buildValue())
                .reduce((acc, value) => ({...acc, [value.fieldName]: value.jsonValue}), {})
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
            ),
            this.id
        )
    }

    static fromJson(json: jsonFormat): OutgoingEventDescription {
        return new OutgoingEventDescription(
            json.type,
            json.descriptors.map((descriptor: any) => fromJsonDescriptor(descriptor))
        );
    }
}