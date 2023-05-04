import {DescriptorJsonType, fromJsonDescriptor} from "@/common/describable-form/field-descriptor.factory";
import {FieldDescriptor} from "@/common/describable-form/field-descriptor";

type FeatureDescriptionJson = {
    type: string,
    id: string,
    descriptors: DescriptorJsonType[]
};

export default class FeatureDescription {
    constructor(
        public type: string,
        public id: string,
        public descriptors: FieldDescriptor<any>[]
    ) {}

    static fromJson(json: FeatureDescriptionJson): FeatureDescription {
        return new FeatureDescription(
            json.type,
            json.id,
            json.descriptors.map(descriptor => fromJsonDescriptor(descriptor))
        );
    }
}

export type FeatureConfiguration = { id: string, [key: string]: unknown }