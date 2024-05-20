import FeatureDescription, {FeatureConfiguration} from "@/features/FeatureDescription";

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

    createFeature(id: string, type: FeatureType): Promise<boolean> {
        return fetch(`${this.backendUrl}/feature/${id}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({type: type})
        }).then((response) => response.ok);
    }

    updateFeature(featureConfiguration: FeatureConfiguration): Promise<boolean> {
        return fetch(`${this.backendUrl}/feature/${featureConfiguration.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(featureConfiguration)
        }).then((response) => response.ok);
    }

    getAllTypes(): Promise<FeatureType[]> {
        return fetch(`${this.backendUrl}/features/types`)
            .then(response => response.json());
    }
}

export type FeatureType = string
