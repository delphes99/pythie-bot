import {Field} from "@/common/dynamicForm/field/Field";

export interface DynamicForm {
    readonly type: string
    readonly fields: Field<any>[]
}