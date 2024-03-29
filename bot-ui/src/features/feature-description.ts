import {FieldDescriptor} from "@/common/describableForm/field-descriptor";
import {DescriptorJsonType, fromJsonDescriptor} from "@/common/describableForm/field-descriptor.factory";
import {FormDescription} from "@/common/describableForm/form-description";

type FeatureDescriptionJson = {
    type: string,
    id: string,
    descriptors: DescriptorJsonType[]
};

export default class FeatureDescription implements FormDescription<FeatureConfiguration> {
    constructor(
        readonly type: string,
        readonly id: string,
        readonly descriptors: FieldDescriptor<any>[]
    ) {
    }

    modifyDescriptor(newDescriptor: FieldDescriptor<any>) {
        return new FeatureDescription(
            this.type,
            this.id,
            this.descriptors.map((d) => {
                if (d.fieldName === newDescriptor.fieldName) {
                    return newDescriptor
                }

                return d
            })
        )
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