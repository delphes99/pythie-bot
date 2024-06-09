import {DynamicForm} from "@/common/dynamicForm/DynamicForm";
import {DynamicFormType} from "@/common/dynamicForm/DynamicFormType";
import {fieldFromJson} from "@/common/dynamicForm/field/Field.factory";

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
                return new DynamicForm(
                    json.type,
                    json.fields.map((descriptor: any) => fieldFromJson(descriptor))
                )
            });
    }
}