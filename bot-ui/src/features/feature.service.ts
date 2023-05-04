import FeatureDescription, {FeatureConfiguration} from "@/features/feature-description";

export default class FeatureService {
    constructor(
        private backendUrl: string,
    ) {
    }

    getFeatureDescription(featureId: string): Promise<FeatureDescription> {
        return fetch(`${this.backendUrl}/feature/${featureId}`)
            .then(response => response.json())
            .then(json => FeatureDescription.fromJson(json));
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
