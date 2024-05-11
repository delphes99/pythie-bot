import {DynamicFormDescription} from "@/common/dynamicForm/dynamic-form-description";
import {DynamicFormType} from "@/common/dynamicForm/dynamic-form-type";
import {fromJsonDescriptor} from "@/common/dynamicForm/field/field-descriptor.factory";

export default class DynamicFormService {
    constructor(
        private backendUrl: string,
    ) {
    }

    async getFormsByTag(tag: string): Promise<DynamicFormType[]> {
        let response = await fetch(`${this.backendUrl}/dynamicForms/tag/${tag}`);
        return await response.json();
    }

    async getForm(value: DynamicFormType): Promise<DynamicFormDescription> {
        return await fetch(`${this.backendUrl}/dynamicForms/form/${value}`)
            .then(response => response.json())
            .then(json => {
                return {
                    type: json.type,
                    descriptors: json.descriptors.map((descriptor: any) => fromJsonDescriptor(descriptor))
                }
            });
    }
}