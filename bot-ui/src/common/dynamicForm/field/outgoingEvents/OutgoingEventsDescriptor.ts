import {FieldDescriptor} from "@/common/dynamicForm/field/FieldDescriptor";
import {DescriptorJsonType} from "@/common/dynamicForm/field/FieldDescriptor.factory";
import {FieldDescriptorType} from "@/common/dynamicForm/field/FieldDescriptorType";
import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import OutgoingEventsDescriptorEditView
    from "@/common/dynamicForm/field/outgoingEvents/OutgoingEventsDescriptorEditView.vue";
import {OutgoingEvent} from "@/features/outgoingevents/OutgoingEvent";
import {OutgoingEventDescription} from "@/features/outgoingevents/OutgoingEventDescription";

export class OutgoingEventsDescriptor implements FieldDescriptor<OutgoingEvent[]> {
    static readonly type: FieldDescriptorType = FieldDescriptorType.OUTGOING_EVENTS

    constructor(
        readonly fieldName: string,
        readonly description: string,
        readonly events: OutgoingEventDescription[]
    ) {
        this.fieldName = fieldName;
        this.description = description;
    }

    deleteEvent(event: OutgoingEventDescription): OutgoingEventsDescriptor {
        return new OutgoingEventsDescriptor(
            this.fieldName,
            this.description,
            this.events.filter((e) => e.id !== event.id)
        )
    }

    modifyEvent(event: OutgoingEventDescription, modification: FieldDescriptor<any>): OutgoingEventsDescriptor {
        return new OutgoingEventsDescriptor(
            this.fieldName,
            this.description,
            this.events.map((e) => {
                if (e.id === event.id) {
                    return e.modifyDescriptor(modification)
                }

                return e
            })
        )
    }

    addEvent(event: OutgoingEventDescription): OutgoingEventsDescriptor {
        return new OutgoingEventsDescriptor(
            this.fieldName,
            this.description,
            this.events.concat(event)
        )
    }

    modifyDescriptor(newDescription: OutgoingEventDescription) {
        return new OutgoingEventsDescriptor(
            this.fieldName,
            this.description,
            this.events.map((e) => {
                if (e.id === newDescription.id) {
                    return newDescription
                }

                return e
            })
        )
    }

    static fromJson(json: DescriptorJsonType): OutgoingEventsDescriptor {
        if (json.type !== this.type) {
            throw new Error(`Cannot deserialize ${json.type} as OutgoingEventsDescriptor`);
        }
        return new OutgoingEventsDescriptor(
            json.fieldName,
            json.description,
            json.value.map((event: any) => OutgoingEventDescription.fromJson(event))
        );
    }

    buildValue(): FieldJsonValue {
        return new FieldJsonValue(this.fieldName, this.events.map(event => event.buildValue()));
    }

    viewComponent() {
        return OutgoingEventsDescriptorEditView
    }
}