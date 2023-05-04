import {FieldDescriptorType} from "@/common/describable-form/field-descriptor-type";
import {FieldDescriptor, FieldValue} from "@/common/describable-form/field-descriptor";
import FieldOutgoingEventsEditView from "@/common/describable-form/outgoingEvents/field-outgoing-events-edit-view.vue";

type JsonFormat = {
    fieldName: String,
    description: String
};

export class OutgoingEventsDescriptor implements FieldDescriptor<Array<String>> {
    description: String;
    fieldName: String;
    type: FieldDescriptorType = FieldDescriptorType.OUTGOING_EVENTS

    constructor(fieldName: String, description: String) {
        this.fieldName = fieldName;
        this.description = description;
    }

    static fromJson(json: JsonFormat): OutgoingEventsDescriptor {
        return new OutgoingEventsDescriptor(
            json.fieldName,
            json.description
        );
    }

    buildValue() {
        return new FieldValue(this.fieldName, []);
    }

    viewComponent() {
        return FieldOutgoingEventsEditView
    }
}