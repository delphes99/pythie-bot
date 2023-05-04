import {FieldDescriptor} from "@/common/describable-form/field-descriptor";
import {fromJsonDescriptor} from "@/common/describable-form/field-descriptor.factory";

type jsonFormat = {
    type: string,
    descriptors: FieldDescriptor<any>[]
};

export class OutgoingEventDescription {
    constructor(
        public type: string,
        public descriptors: FieldDescriptor<any>[]
    ) {
    }

    static fromJson(json: jsonFormat): OutgoingEventDescription {
        return new OutgoingEventDescription(
            json.type,
            json.descriptors.map((descriptor: any) => fromJsonDescriptor(descriptor))
        );
    }
}