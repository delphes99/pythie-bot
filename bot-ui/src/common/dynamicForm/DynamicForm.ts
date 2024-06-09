import {Field} from "@/common/dynamicForm/field/Field";

export class DynamicForm {
    readonly id: string = crypto.randomUUID()

    constructor(
        readonly type: string,
        readonly fields: Field<any>[],
    ) {

    }

    buildJson(): any {
        return {
            id: this.id,
            type: this.type,
            fields: this.fields.map(field => field.buildJson())
        }
    }
}