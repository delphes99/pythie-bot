import {DescriptorJsonType, fromJsonDescriptor} from "@/features/descriptor-factory";
import {FieldDescriptor} from "@/common/describable-form/field-descriptor";

type FeatureDescriptionJson = {
    type: string,
    id: string,
    descriptors: Array<DescriptorJsonType>
};

export default class FeatureDescription {
    type: string
    id: string
    descriptors: FieldDescriptor<any>[]

    constructor(type: string, id: string, descriptors: FieldDescriptor<any>[]) {
        this.type = type;
        this.id = id;
        this.descriptors = descriptors;
    }

    static fromJson(json: FeatureDescriptionJson): FeatureDescription {
        return new FeatureDescription(
            json.type,
            json.id,
            json.descriptors.map(descriptor => fromJsonDescriptor(descriptor))
        );
    }
}

export type FeatureConfiguration = { id: string, [key: string]: unknown }