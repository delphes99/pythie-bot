import {FieldDescriptor, FieldValue} from "@/common/dynamicForm/field/field-descriptor";
import {FieldDescriptorType} from "@/common/dynamicForm/field/field-descriptor-type";
import {DescriptorJsonType} from "@/common/dynamicForm/field/field-descriptor.factory";
import OutgoingEventsDescriptorEditView
    from "@/common/dynamicForm/field/outgoingEvents/outgoing-events-descriptor-edit-view.vue";
import {OutgoingEvent} from "@/features/outgoingevents/outgoing-event";
import {OutgoingEventDescription} from "@/features/outgoingevents/outgoing-event-description";

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

    buildValue(): FieldValue<OutgoingEvent[]> {
        return new FieldValue(this.fieldName, this.events.map(event => event.buildValue()));
    }

    viewComponent() {
        return OutgoingEventsDescriptorEditView
    }
}