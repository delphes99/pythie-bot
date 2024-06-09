import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";

//TODO: Remove this
export interface FieldDescriptor<T> {
    readonly fieldName: string
    readonly description: string

    buildValue(): FieldJsonValue

    viewComponent(): any
}