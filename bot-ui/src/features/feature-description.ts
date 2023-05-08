import {FieldDescriptor} from "@/common/describable-form/field-descriptor";
import {DescriptorJsonType, fromJsonDescriptor} from "@/common/describable-form/field-descriptor.factory";
import {FormDescription} from "@/common/describable-form/form-description";

type FeatureDescriptionJson = {
    type: string,
    id: string,
    descriptors: DescriptorJsonType[]
};

export default class FeatureDescription implements FormDescription<FeatureConfiguration> {
    constructor(
        public type: string,
        public id: string,
        public descriptors: FieldDescriptor<any>[]
    ) {
    }

    buildValue(): FeatureConfiguration {
        return {
            type: this.type,
            id: this.id,
            ...this.descriptors
                .map(descriptor => descriptor.buildValue())
                .reduce((acc, value) => ({...acc, [value.fieldName]: value.value}), {})
        }
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