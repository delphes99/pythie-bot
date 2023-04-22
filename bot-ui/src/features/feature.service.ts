import FeatureDescription, {FeatureConfiguration} from "@/features/feature-description";
import {FieldDescriptor} from "@/common/describable-form/field-descriptor";

export default class FeatureService {
    constructor(
        private backendUrl: string,
    ) {
    }

    getFeatureDescription(featureId: string): Promise<FeatureDescription> {
        return fetch(`${this.backendUrl}/feature/${featureId}`)
            .then((response) => response.json())
            .then((json) => new FeatureDescription(json.type,
                json.id,
                json.descriptors.map((descriptor: FieldDescriptor) => new FieldDescriptor(
                    descriptor.fieldName,
                    descriptor.description,
                    descriptor.type,
                    descriptor.value
                ))
            ));
    }

    updateFeature(featureConfiguration: FeatureConfiguration): Promise<boolean> {
        return fetch(`${this.backendUrl}/feature/${featureConfiguration.id}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(featureConfiguration)
        }).then((response) => response.ok);
    }
}
