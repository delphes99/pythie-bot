import {Field} from "@/common/dynamicForm/field/Field";

export class DynamicForm {
    constructor(
        readonly type: string,
        readonly fields: Field<any>[],
        readonly id: string = crypto.randomUUID()
    ) {

    }

    buildJson(): any {
        return {
            id: this.id,
            type: this.type,
            ...this.fields
                .map(field => field.buildJsonValue())
                .reduce((acc, value) => ({...acc, [value.fieldName]: value.jsonValue}), {})
        }
    }
}