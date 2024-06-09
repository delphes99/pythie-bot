import {FieldJsonValue} from "@/common/dynamicForm/field/FieldJsonValue";
import {Component} from "vue";

export interface Field<T> {
    readonly id: string
    readonly fieldName: string
    readonly description: string

    readonly initialValue: T
    actualValue: T

    viewComponent(): Component

    buildJsonValue(): FieldJsonValue
}