import {Component} from "vue";

export interface Field<T> {
    fieldName: string
    description: string

    initialValue: T
    actualValue: T

    viewComponent(): Component
}