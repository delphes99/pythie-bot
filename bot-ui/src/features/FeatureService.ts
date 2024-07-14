import {DynamicForm} from "@/common/dynamicForm/DynamicForm";
import DynamicFormService from "@/common/dynamicForm/DynamicFormService";
import FeatureDescription from "@/features/FeatureDescription";
import {FeatureSummary} from "@/features/FeatureSummary";

export default class FeatureService {
    constructor(
        private backendUrl: string,
        private dynamicFormService: DynamicFormService
    ) {
    }

    async getAllFeatures(): Promise<FeatureSummary[]> {
        return fetch(`${this.backendUrl}/features`)
            .then(response => response.json());
    }

    async getFeatureDescription(featureId: string): Promise<FeatureDescription> {
        return fetch(`${this.backendUrl}/feature/${featureId}`)
            .then(response => response.json())
            .then(json => {
                return {
                    id: json.id,
                    definition: this.dynamicFormService.buildForm(json.definition)
                }
            });
    }

    async createFeature(id: string, type: FeatureType): Promise<boolean> {
        return fetch(`${this.backendUrl}/feature/${id}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({type: type})
        }).then((response) => response.ok);
    }

    async updateFeature(id: string, form: DynamicForm): Promise<boolean> {
        const payload = form.buildJson();
        return fetch(`${this.backendUrl}/feature/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload)
        }).then((response) => response.ok);
    }

    async getAllTypes(): Promise<FeatureType[]> {
        return fetch(`${this.backendUrl}/features/types`)
            .then(response => response.json());
    }
}

export type FeatureType = string
