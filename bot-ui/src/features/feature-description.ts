import {FieldDescriptor} from "@/common/describable-form/field-descriptor";

export default class FeatureDescription {
    type: string
    id: string
    descriptors: FieldDescriptor[]

    constructor(type: string, id: string, descriptors: FieldDescriptor[]) {
        this.type = type;
        this.id = id;
        this.descriptors = descriptors;
    }
}

export type FeatureConfiguration = { id: string, [key: string]: string }