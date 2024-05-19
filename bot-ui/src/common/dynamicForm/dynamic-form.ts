import {Field} from "@/common/dynamicForm/field/field";

export interface DynamicForm {
    readonly type: string
    readonly fields: Field<any>[]
}