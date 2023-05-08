import {FieldDescriptor, FieldValue} from "@/common/describable-form/field-descriptor";
import {FieldDescriptorType} from "@/common/describable-form/field-descriptor-type";
import {DescriptorJsonType} from "@/common/describable-form/field-descriptor.factory";
import FieldOutgoingEventsEditView from "@/common/describable-form/outgoingEvents/field-outgoing-events-edit-view.vue";
import {OutgoingEvent} from "@/features/outgoingevents/outgoing-event";
import {OutgoingEventDescription} from "@/features/outgoingevents/outgoing-event-description";

export class OutgoingEventsDescriptor implements FieldDescriptor<OutgoingEvent[]> {
    type: FieldDescriptorType = FieldDescriptorType.OUTGOING_EVENTS

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

    static fromJson(json: DescriptorJsonType): OutgoingEventsDescriptor {
        if (json.type !== FieldDescriptorType.OUTGOING_EVENTS) {
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
        return FieldOutgoingEventsEditView
    }
}