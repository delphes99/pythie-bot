import FeatureDescription, {FeatureConfiguration} from "@/features/feature-description";

export default class FeatureDescriptionService {
    buildConfiguration(description: FeatureDescription, ...modifications: SetValue[]): FeatureConfiguration {
        const payload: FeatureConfiguration = {
            type: description.type,
            id: description.id
        };

        description.descriptors.forEach(descriptor => {
            payload[descriptor.fieldName] = descriptor.value
        })

        modifications.forEach(modification => {
            payload[modification.fieldName] = modification.value
        })

        return payload
    }
}

export class SetValue {
    fieldName: string
    value: string

    constructor(fieldName: string, value: string) {
        this.fieldName = fieldName;
        this.value = value;
    }
}