import {DynamicForm} from "@/common/dynamicForm/dynamic-form";
import {DynamicFormType} from "@/common/dynamicForm/dynamic-form-type";
import {fieldFromJson} from "@/common/dynamicForm/field/field.factory";

export default class DynamicFormService {
    constructor(
        private backendUrl: string,
    ) {
    }

    async getFormsByTag(tag: string): Promise<DynamicFormType[]> {
        let response = await fetch(`${this.backendUrl}/dynamicForms/tag/${tag}`);
        return await response.json();
    }

    async getForm(value: DynamicFormType): Promise<DynamicForm> {
        return await fetch(`${this.backendUrl}/dynamicForms/form/${value}`)
            .then(response => response.json())
            .then(json => {
                return {
                    type: json.type,
                    fields: json.fields.map((descriptor: any) => fieldFromJson(descriptor))
                }
            });
    }
}