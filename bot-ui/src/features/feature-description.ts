export default class FeatureDescription {
    type: string
    id: string
    descriptors: FeatureDescriptor[]

    constructor(type: string, id: string, descriptors: FeatureDescriptor[]) {
        this.type = type;
        this.id = id;
        this.descriptors = descriptors;
    }
}

export class FeatureDescriptor {
    fieldName: string
    description: string
    type: string
    value: string

    constructor(fieldName: string, description: string, type: string, value: string) {
        this.fieldName = fieldName;
        this.description = description;
        this.type = type;
        this.value = value;
    }
}

export type FeatureConfiguration = { id: string, [key: string]: string }