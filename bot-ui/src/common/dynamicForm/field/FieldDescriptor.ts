export class FieldValue<T> {
    constructor(
        readonly fieldName: string,
        readonly value: T
    ) {
    }
}

//TODO: Remove this
export interface FieldDescriptor<T> {
    readonly fieldName: string
    readonly description: string

    buildValue(): FieldValue<T>

    viewComponent(): any
}